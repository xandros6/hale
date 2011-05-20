/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.hale.task.providers.schema;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import eu.esdihumboldt.hale.Messages;
import eu.esdihumboldt.hale.models.AlignmentService;
import eu.esdihumboldt.hale.schemaprovider.model.AttributeDefinition;
import eu.esdihumboldt.hale.schemaprovider.model.Definition;
import eu.esdihumboldt.hale.schemaprovider.model.SchemaElement;
import eu.esdihumboldt.hale.schemaprovider.model.TypeDefinition;
import eu.esdihumboldt.hale.task.ServiceProvider;
import eu.esdihumboldt.hale.task.Task;
import eu.esdihumboldt.hale.task.TaskFactory;
import eu.esdihumboldt.hale.task.TaskType;
import eu.esdihumboldt.hale.task.impl.AbstractTaskFactory;
import eu.esdihumboldt.hale.task.impl.AbstractTaskType;
import eu.esdihumboldt.hale.task.impl.AlignmentTask;
import eu.esdihumboldt.specification.cst.align.ICell;

/**
 * Map attribute task factory
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class MapNilAttributeTaskFactory extends AbstractTaskFactory {
	
	/**
	 * The task 
	 */
	private class MapNilAttributeTask extends AlignmentTask {

		/**
		 * Create a new task
		 *
		 * @param serviceProvider the service provider 
		 * @param type the type to map
		 */
		public MapNilAttributeTask(ServiceProvider serviceProvider,
				AttributeDefinition type) {
			super(serviceProvider, getTaskTypeName(), Collections.singletonList(type));
		}

		/**
		 * @see AlignmentTask#cellsAdded(Iterable)
		 */
		@Override
		public void cellsAdded(Iterable<ICell> cells) {
			//TODO check given cells instead of calling validateTask
			if (!validateTask((AttributeDefinition) getMainContext(), alignmentService)) {
				invalidate();
			}
		}

		/**
		 * @see AlignmentTask#cellRemoved(ICell)
		 */
		@Override
		public void cellRemoved(ICell cell) {
			if (!validateTask((AttributeDefinition) getMainContext(), alignmentService)) {
				invalidate();
			}
		}

	}

	/**
	 * The task type
	 */
	private static class MapNilAttributeTaskType extends AbstractTaskType {
		
		/**
		 * Constructor
		 * 
		 * @param taskFactory the task factory
		 */
		public MapNilAttributeTaskType(TaskFactory taskFactory) {
			super(taskFactory);
		}

		/**
		 * @see TaskType#getReason(Task)
		 */
		@Override
		public String getReason(Task task) {
			return Messages.MapNilAttributeTaskFactory_0; //$NON-NLS-1$
		}

		/**
		 * @see TaskType#getSeverityLevel(Task)
		 */
		@Override
		public SeverityLevel getSeverityLevel(Task task) {
			return SeverityLevel.warning;
		}

		/**
		 * @see TaskType#getTitle(Task)
		 */
		@Override
		public String getTitle(Task task) {
			return MessageFormat.format(Messages.MapNilAttributeTaskFactory_1, ((AttributeDefinition) task.getMainContext()).getName()); //$NON-NLS-1$
		}

		/**
		 * @see TaskType#getValue(Task)
		 */
		@Override
		public double getValue(Task task) {
			return 0.4;
		}

	}
	
	/**
	 * The type name
	 */
	public static final String BASE_TYPE_NAME = "Schema.mapNilAttribute"; //$NON-NLS-1$
	
	/**
	 * The task type
	 */
	private final TaskType type;

	/**
	 * Default constructor
	 */
	public MapNilAttributeTaskFactory() {
		super(BASE_TYPE_NAME);
		
		type = new MapNilAttributeTaskType(this);
	}

	/**
	 * @see TaskFactory#createTask(ServiceProvider, Definition[])
	 */
	@Override
	public Task createTask(ServiceProvider serviceProvider,
			Definition... definitions) {
		if (definitions == null || definitions.length < 1 || !(definitions[0] instanceof AttributeDefinition)) {
			return null;
		}
		
		AlignmentService alignmentService = serviceProvider.getService(AlignmentService.class);
		
		AttributeDefinition type = (AttributeDefinition) definitions[0];
		if (validateTask(type, alignmentService)) {
			return new MapNilAttributeTask(serviceProvider, type);
		}
		
		return null;
	}

	/**
	 * Determines if the given attribute definition is valid for a task
	 * 
	 * @param attribute the attribute definition
	 * @param alignmentService the alignment service
	 * 
	 * @return if the type is valid
	 */
	private static boolean validateTask(AttributeDefinition attribute,
			AlignmentService alignmentService) {
		if (attribute.getMinOccurs() == 0 || attribute.isNillable()) {
			return false;
		}
		
		// additional condition: declaring type or sub type must be mapped
		boolean typeMapped = false;
		Queue<TypeDefinition> typeQueue = new LinkedList<TypeDefinition>();
		typeQueue.add(attribute.getDeclaringType());
		while (!typeMapped && !typeQueue.isEmpty()) {
			TypeDefinition type = typeQueue.poll();
			
			for (SchemaElement element : type.getDeclaringElements()) {
				List<ICell> elementCells = alignmentService.getCell(element.getEntity());
				if (elementCells != null && !elementCells.isEmpty()) {
					typeMapped = true;
				}
			}
			
			typeQueue.addAll(type.getSubTypes());
		}
		if (!typeMapped) {
			return false;
		}
		
		//TODO check for nil reason?
		
		List<ICell> attributeCells = alignmentService.getCell(attribute.getEntity());
		if (attributeCells == null || attributeCells.isEmpty()) {
			return true;
		}
		
		return false;
	}

	/**
	 * @see TaskFactory#getTaskType()
	 */
	@Override
	public TaskType getTaskType() {
		return type;
	}

}
