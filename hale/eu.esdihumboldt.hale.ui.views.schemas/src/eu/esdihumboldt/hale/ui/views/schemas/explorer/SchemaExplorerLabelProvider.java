/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2011.
 */

package eu.esdihumboldt.hale.ui.views.schemas.explorer;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;

import eu.esdihumboldt.hale.schema.model.ChildDefinition;
import eu.esdihumboldt.hale.schema.model.constraint.property.Cardinality;
import eu.esdihumboldt.hale.ui.common.definition.viewer.DefinitionLabelProvider;

/**
 * Extended label provider for definitions
 * @author Simon Templer
 */
public class SchemaExplorerLabelProvider extends StyledCellLabelProvider {
	
	private final DefinitionLabelProvider defaultLabels = new DefinitionLabelProvider();

	/**
	 * @see StyledCellLabelProvider#update(ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		
		StyledString text = new StyledString(defaultLabels.getText(element));
		
		// append cardinality
		if (element instanceof ChildDefinition<?>) {
			Cardinality cardinality = null;
			if (((ChildDefinition<?>) element).asGroup() != null) {
				cardinality = ((ChildDefinition<?>) element).asGroup().getConstraint(Cardinality.class);
			}
			else if (((ChildDefinition<?>) element).asProperty() != null) {
				cardinality = ((ChildDefinition<?>) element).asProperty().getConstraint(Cardinality.class);
			}
			
			if (cardinality != null) {
				// only append cardinality if it isn't 1/1
				if (cardinality.getMinOccurs() != 1 || cardinality.getMaxOccurs() != 1) {
					String card = " " + MessageFormat.format("({0}..{1})", 
							new Object[]{Long.valueOf(cardinality.getMinOccurs()), 
							(cardinality.getMaxOccurs() == Cardinality.UNBOUNDED)?("n"):(Long.valueOf(cardinality.getMaxOccurs()))});
					text.append(card, StyledString.COUNTER_STYLER);
				}
			}
		}
		
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		
		cell.setImage(defaultLabels.getImage(element));
		
		super.update(cell);
	}

	/**
	 * @see StyledCellLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		defaultLabels.dispose();
		
		super.dispose();
	}

}