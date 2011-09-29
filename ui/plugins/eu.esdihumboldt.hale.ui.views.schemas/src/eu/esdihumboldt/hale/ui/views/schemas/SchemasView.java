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
package eu.esdihumboldt.hale.ui.views.schemas;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.WorkbenchPart;

import de.cs3d.util.logging.ALogger;
import de.cs3d.util.logging.ALoggerFactory;

import eu.esdihumboldt.hale.common.align.model.EntityDefinition;
import eu.esdihumboldt.hale.common.align.model.impl.PropertyEntityDefinition;
import eu.esdihumboldt.hale.common.align.model.impl.TypeEntityDefinition;
import eu.esdihumboldt.hale.common.schema.model.Definition;
import eu.esdihumboldt.hale.common.schema.model.PropertyDefinition;
import eu.esdihumboldt.hale.common.schema.model.Schema;
import eu.esdihumboldt.hale.common.schema.model.TypeDefinition;
import eu.esdihumboldt.hale.ui.function.contribution.SchemaSelectionFunctionContribution;
import eu.esdihumboldt.hale.ui.selection.SchemaSelection;
import eu.esdihumboldt.hale.ui.selection.impl.DefaultSchemaSelection;
import eu.esdihumboldt.hale.ui.selection.impl.DefaultSchemaSelection.SchemaStructuredMode;
import eu.esdihumboldt.hale.ui.service.schema.SchemaService;
import eu.esdihumboldt.hale.ui.service.schema.SchemaServiceListener;
import eu.esdihumboldt.hale.ui.service.schema.SchemaSpaceID;
import eu.esdihumboldt.hale.ui.views.properties.PropertiesViewPart;
import eu.esdihumboldt.hale.ui.views.schemas.explorer.SchemaExplorer;
import eu.esdihumboldt.hale.ui.views.schemas.internal.Messages;
import eu.esdihumboldt.hale.ui.views.schemas.internal.SchemasViewPlugin;

/**
 * This view component handles the display of source and target schemas.
 * @author Thorsten Reitz
 * @author Simon Templer
 */
public class SchemasView extends PropertiesViewPart {

	private static final ALogger log = ALoggerFactory.getLogger(SchemasView.class);
	
	/**
	 * Function contribution that always uses this view's selection
	 */
	private class SchemaFunctionContribution extends SchemaSelectionFunctionContribution {

		/**
		 * Default constructor
		 */
		public SchemaFunctionContribution() {
			super();
		}

		/**
		 * @see SchemaSelectionFunctionContribution#getSelection()
		 */
		@Override
		public SchemaSelection getSelection() {
			if (currentSelection instanceof SchemaSelection) {
				return (SchemaSelection) currentSelection;
			}
			
			return super.getSelection();
		}

	}

	/**
	 * Selection provider combining selections from source and target schema explorers
	 */
	private class SchemasSelectionProvider implements ISelectionProvider, ISelectionChangedListener {
		
		/**
		 * The selection listeners
		 */
		private final Set<ISelectionChangedListener> listeners = new HashSet<ISelectionChangedListener>();

		private boolean lastSourceFirst = true;

		/**
		 * Default constructor
		 */
		public SchemasSelectionProvider() {
			super();
			
			sourceExplorer.getTreeViewer().addSelectionChangedListener(this);
			targetExplorer.getTreeViewer().addSelectionChangedListener(this);
		}

		/**
		 * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
		 */
		@Override
		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			listeners.add(listener);
		}

		/**
		 * @see ISelectionProvider#getSelection()
		 */
		@Override
		public ISelection getSelection() {
			return currentSelection;
		}

		/**
		 * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
		 */
		@Override
		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			listeners.remove(listener);
		}

		/**
		 * @see ISelectionProvider#setSelection(ISelection)
		 */
		@Override
		public void setSelection(ISelection selection) {
			SchemasView.this.currentSelection = selection;
		}
		
		/**
		 * Update the selection and fire a selection change
		 * @param sourceFirst if the selected objects from the source shall
		 *   be added first if the selection is a combination from source and
		 *   target
		 */
		private void updateSelection(boolean sourceFirst) {
			lastSourceFirst = sourceFirst;
			
			// combine the selections of both viewers
			
			// source items
			ITreeSelection sourceSelection = (ITreeSelection) sourceExplorer.getTreeViewer().getSelection();
			// target items
			ITreeSelection targetSelection = (ITreeSelection) targetExplorer.getTreeViewer().getSelection();

			/*
			 * XXX because there are problem with the properties view if we 
			 * combine the objects here (multiple objects in the selection), 
			 * we return only one of the original selections
			 */
			SchemaStructuredMode selectionMode = (sourceFirst)?
					(SchemaStructuredMode.ONLY_SOURCE):
					(SchemaStructuredMode.ONLY_TARGET);
					
			Collection<EntityDefinition> sourceItems = collectDefinitions(sourceSelection);
			Collection<EntityDefinition> targetItems = collectDefinitions(targetSelection);
			DefaultSchemaSelection selection = new DefaultSchemaSelection(
					sourceItems, targetItems, selectionMode);
			
			fireSelectionChange(selection);
		}
		
		/**
		 * Collect {@link EntityDefinition} from a {@link TreeSelection} 
		 * containing {@link TypeDefinition}s and {@link PropertyDefinition}s
		 * @param selection the tree selection
		 * @return the collected entity definitions
		 */
		private Collection<EntityDefinition> collectDefinitions(
				ITreeSelection selection) {
			if (selection.isEmpty()) {
				return Collections.emptyList();
			}
			
			TreePath[] paths = selection.getPaths();
			List<EntityDefinition> result = new ArrayList<EntityDefinition>(paths.length);
			
			for (TreePath path : paths) {
				Object last = path.getLastSegment();
				if (last instanceof TypeDefinition) {
					result.add(new TypeEntityDefinition((TypeDefinition) last));
				}
				else if (last instanceof PropertyDefinition) {
					List<Definition<?>> propertyPath = new ArrayList<Definition<?>>();
					Definition<?> element = (Definition<?>) last;
					int index = path.getSegmentCount() - 1;
					while (element != null && !(element instanceof TypeDefinition)) {
						propertyPath.add(0, element);
						Object segment = path.getSegment(--index);
						if (segment instanceof Definition<?>) {
							element = (Definition<?>) segment;
						}
						else {
							element = null;
						}
					}
					
					if (element != null) {
						// prepend type definition to path 
						propertyPath.add(0, element);
						result.add(new PropertyEntityDefinition(propertyPath));
					}
					else {
						log.error("No parent type definition for property path found, skipping object for selection.");
					}
				}
				else {
					log.error("Could determine entity definition for object, skipping object for selection.");
				}
			}
			
			return result;
		}

		/**
		 * Sets the selection to the given selection and fires a selection change
		 * 
		 * @param selection the selection to set 
		 */
		protected void fireSelectionChange(ISelection selection) {
			SchemasView.this.currentSelection = selection;
			
			SelectionChangedEvent event = 
				new SelectionChangedEvent(this, currentSelection);
			
			for (ISelectionChangedListener listener : listeners) {
				listener.selectionChanged(event);
			}
		}

		/**
		 * @see ISelectionChangedListener#selectionChanged(SelectionChangedEvent)
		 */
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			updateSelection(event.getSelectionProvider() == sourceExplorer.getTreeViewer());
		}

		/**
		 * @return the lastSourceFirst
		 */
		public boolean isLastSourceFirst() {
			return lastSourceFirst;
		}

	}

	/**
	 * The view id
	 */
	public static final String ID = "eu.esdihumboldt.hale.ui.views.schemas"; //$NON-NLS-1$
	
	/**
	 * The current selection
	 */
	private ISelection currentSelection;

	/**
	 * Viewer for the source schema
	 */
	private SchemaExplorer sourceExplorer;
	
	/**
	 * Viewer for the target schema
	 */
	private SchemaExplorer targetExplorer;

	/**
	 * A reference to the {@link SchemaService} which serves the model for this
	 * view.
	 */
	private SchemaService schemaService;

	private Image functionImage;
	
	private Image augmentImage;

	private SchemaServiceListener schemaListener;

	private SchemasSelectionProvider selectionProvider;

//	private HaleServiceListener alignmentListener;
//
//	private StyleServiceListener styleListener;
	
	/**
	 * @see WorkbenchPart#createPartControl(Composite)
	 */
	@Override
	public void createPartControl(Composite _parent) {
		// get schema service
		schemaService = (SchemaService) PlatformUI.getWorkbench().getService(SchemaService.class);
		schemaService.addSchemaServiceListener(schemaListener = new SchemaServiceListener() {
			
			@Override
			public void schemasCleared(final SchemaSpaceID spaceID) {
				final Display display = PlatformUI.getWorkbench().getDisplay();
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						switch (spaceID) {
						case SOURCE:
							sourceExplorer.setSchema(null);
							break;
						case TARGET:
							targetExplorer.setSchema(null);
						}
					}
				});
			}
			
			@Override
			public void schemaAdded(final SchemaSpaceID spaceID, Schema schema) {
				final Display display = PlatformUI.getWorkbench().getDisplay();
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						switch (spaceID) {
						case SOURCE:
							sourceExplorer.setSchema(schemaService.getSchemas(spaceID));
							break;
						case TARGET:
							targetExplorer.setSchema(schemaService.getSchemas(spaceID));
						}
					}
				});
			}
		});
		
		Composite modelComposite = new Composite(_parent, SWT.BEGINNING);
		GridLayout layout = new GridLayout(3, false);
		layout.verticalSpacing = 3;
		layout.horizontalSpacing = 0;
		modelComposite.setLayout(layout);
		
		// source schema toolbar, filter and explorer
		sourceExplorer = new SchemaExplorer(modelComposite, "Source");
		sourceExplorer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		
		// function button
		final Button functionButton = new Button(modelComposite, SWT.PUSH | SWT.FLAT);
		functionImage = SchemasViewPlugin.getImageDescriptor("icons/mapping.gif").createImage(); //$NON-NLS-1$
		augmentImage = SchemasViewPlugin.getImageDescriptor("icons/augment.gif").createImage(); //$NON-NLS-1$
		functionButton.setImage(functionImage);
		functionButton.setToolTipText(Messages.ModelNavigationView_FunctionButtonToolTipText);
		functionButton.setEnabled(false);
		functionButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		MenuManager manager = new MenuManager();
		manager.setRemoveAllWhenShown(true);
		final SchemaFunctionContribution functionContribution = new SchemaFunctionContribution();
		manager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				// populate context menu
				manager.add(functionContribution);
			}
			
		});
		final Menu functionMenu = manager.createContextMenu(functionButton);
		functionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// show menu on button press
				functionMenu.setLocation(Display.getCurrent().getCursorLocation());
				functionMenu.setVisible(true);
			}
			
		});

		// target schema toolbar, filter and explorer
		targetExplorer = new SchemaExplorer(modelComposite, "Target");
		targetExplorer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		
		// redraw on alignment change
//		AlignmentService as = (AlignmentService) getSite().getService(AlignmentService.class);
//		as.addListener(alignmentListener = new HaleServiceListener() {
//
//			@Override
//			public void update(@SuppressWarnings("rawtypes") UpdateMessage message) {
//				refreshInDisplayThread();
//			}
//			
//		});
		
		// also add the alignment listener to the style service (for refreshing icons when style changes)
//		StyleService styleService = (StyleService) PlatformUI.getWorkbench().getService(StyleService.class);
//		styleService.addListener(styleListener = new StyleServiceListener() {
//			
//			@Override
//			public void stylesRemoved(StyleService styleService) {
//				// refresh to update legend images
//				refreshInDisplayThread();
//			}
//			
//			@Override
//			public void stylesAdded(StyleService styleService) {
//				// refresh to update legend images
//				refreshInDisplayThread();
//			}
//			
//			@Override
//			public void backgroundChanged(StyleService styleService, RGB background) {
//				// refresh to update legend images
//				refreshInDisplayThread();
//			}
//
//			@Override
//			public void styleSettingsChanged(StyleService styleService) {
//				// refresh to update legend images
//				refreshInDisplayThread();
//			}
//		});
		
//		MenuManager sourceMenuManager = new MenuManager();
//		sourceMenuManager.setRemoveAllWhenShown(true);
//		final IContributionItem sourceContextFunctions = new SchemaItemContribution(sourceSchemaViewer, false);
//		sourceMenuManager.addMenuListener(new IMenuListener() {
//
//			@Override
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.add(sourceContextFunctions);
//			}
//			
//		});
//		
//		Menu sourceMenu = sourceMenuManager.createContextMenu(sourceSchemaViewer.getControl());
//		sourceSchemaViewer.getControl().setMenu(sourceMenu);
//		
//		MenuManager targetMenuManager = new MenuManager();
//		targetMenuManager.setRemoveAllWhenShown(true);
//		final IContributionItem targetContextFunctions = new SchemaItemContribution(targetSchemaViewer, true);
//		targetMenuManager.addMenuListener(new IMenuListener() {
//
//			@Override
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.add(targetContextFunctions);
//			}
//			
//		});
//		
//		Menu targetMenu = targetMenuManager.createContextMenu(targetSchemaViewer.getControl());
//		targetSchemaViewer.getControl().setMenu(targetMenu);
//		
//		getSite().registerContextMenu(sourceMenuManager, sourceSchemaViewer);
//		getSite().registerContextMenu(targetMenuManager, targetSchemaViewer);

		// initialization of explorers
		sourceExplorer.setSchema(schemaService.getSchemas(SchemaSpaceID.SOURCE));
		targetExplorer.setSchema(schemaService.getSchemas(SchemaSpaceID.TARGET));
		
		// register selection provider
		getSite().setSelectionProvider(selectionProvider = new SchemasSelectionProvider());
		
		// listen for selection changes and update function button
		selectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				functionButton.setEnabled(functionContribution.hasActiveFunctions());
				if (event.getSelection() instanceof SchemaSelection) {
					SchemaSelection selection = (SchemaSelection) event.getSelection();
					if (selection.getSourceItems().size() == 0 && selection.getTargetItems().size() > 0) {
						// augmentation
						functionButton.setImage(augmentImage);
					}
					else {
						// function
						functionButton.setImage(functionImage);
					}
				}
			}
			
		});
	}

//	/**
//	 * Refresh map in the display thread
//	 */
//	protected void refreshInDisplayThread() {
//		if (Display.getCurrent() != null) {
//			refresh();
//		}
//		else {
//			final Display display = PlatformUI.getWorkbench().getDisplay();
//			display.syncExec(new Runnable() {
//				
//				@Override
//				public void run() {
//					refresh();
//				}
//			});
//		}
//	}

	/**
	 * @see WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (selectionProvider != null && !selectionProvider.isLastSourceFirst()) {
			targetExplorer.getTreeViewer().getControl().setFocus();
		}
		else {
			sourceExplorer.getTreeViewer().getControl().setFocus();
		}
	}

//	/**
//	 * Update the viewers on changes to a schema
//	 * 
//	 * @param schema the schema that changed
//	 */
//	protected void update(SchemaSpaceID schema) {
//		switch (schema) {
//		case SOURCE:
//			sourceSchemaViewer.setInput(schemaItemService.getRoot(schema));
//			sourceSchemaViewer.refresh();
//			break;
//		case TARGET:
//			targetSchemaViewer.setInput(schemaItemService.getRoot(schema));
//			targetSchemaViewer.refresh();
//			break;
//		}
//	}

//	/**
//	 * Select the item with representing the given identifier
//	 * 
//	 * @param identifier the identifier
//	 */
//	public void selectItem(String identifier) {
//		TreeViewer tree;
//		SchemaItem item = schemaItemService.getSchemaItem(identifier, SchemaSpaceID.SOURCE);
//		if (item == null) {
//			item = schemaItemService.getSchemaItem(identifier, SchemaSpaceID.TARGET);
//			tree = targetSchemaViewer;
//		}
//		else {
//			tree = sourceSchemaViewer;
//		}
//		
//		if (item != null) {
//			tree.setSelection(new StructuredSelection(item), true);
//		}
//	}
	
	/**
	 * @see WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (schemaListener != null) {
			schemaService.removeSchemaServiceListener(schemaListener);
		}
		
//		if (alignmentListener != null) {
//			AlignmentService as = (AlignmentService) getSite().getService(AlignmentService.class);
//			as.removeListener(alignmentListener);
//			StyleService styleService = (StyleService) PlatformUI.getWorkbench().getService(StyleService.class);
//			styleService.removeListener(styleListener);
//		}
		
		if (functionImage != null) {
			functionImage.dispose();
		}
		if (augmentImage != null) {
			augmentImage.dispose();
		}
		
		super.dispose();
	}

//	/**
//	 * Refresh both tree viewers
//	 */
//	public void refresh() {
//		sourceSchemaViewer.refresh();
//		targetSchemaViewer.refresh();
//	}

}