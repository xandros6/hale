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
package eu.esdihumboldt.hale.rcp.wizards.functions.core.geometric;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import eu.esdihumboldt.hale.rcp.wizards.functions.core.Messages;
import eu.esdihumboldt.hale.rcp.wizards.functions.core.math.MathExpressionFieldEditor;
import eu.esdihumboldt.hale.ui.common.definition.DefinitionLabelFactory;
import eu.esdihumboldt.hale.ui.model.functions.AbstractSingleCellWizardPage;
import eu.esdihumboldt.hale.ui.model.schema.SchemaItem;

/**
 * The WizardPage that contains the UI for the {@link NetworkExpansionFunctionWizard}.
 * 
 * @author Thorsten Reitz
 */
public class NetworkExpansionFunctionWizardPage 
	extends AbstractSingleCellWizardPage {
	
	private MathExpressionFieldEditor expressionEditor = null;
	
	private String initialExpression = null;
	
	/**
	 * Constructor
	 * 
	 * @param pageName the page name
	 */
	protected NetworkExpansionFunctionWizardPage(String pageName) {
		super(pageName);
		
		setTitle(pageName);
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		 super.initializeDialogUnits(parent);
        this.setPageComplete(this.isPageComplete());
        
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        composite.setFont(parent.getFont());

        this.createConfigurationGroup(composite);
        
        setErrorMessage(null);	// should not initially have error message
		super.setControl(composite);

	}

	private void createConfigurationGroup(Composite parent) {
		// define source group composite
		Group configurationGroup = new Group(parent, SWT.NONE);
		configurationGroup.setText(Messages.NetworkExpansionFunctionWizardPage_0);
		configurationGroup.setLayout(new GridLayout());
		GridData configurationAreaGD = new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL);
		configurationAreaGD.grabExcessHorizontalSpace = true;
		configurationGroup.setLayoutData(configurationAreaGD);
		configurationGroup.setSize(configurationGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		configurationGroup.setFont(parent.getFont());
		
		final Composite configurationComposite = new Composite(configurationGroup, SWT.NONE);
		configurationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		GridLayout fileSelectionLayout = new GridLayout();
		fileSelectionLayout.numColumns = 3;
		fileSelectionLayout.makeColumnsEqualWidth = false;
		fileSelectionLayout.marginWidth = 0;
		fileSelectionLayout.marginHeight = 0;
		configurationComposite.setLayout(fileSelectionLayout);
		
		DefinitionLabelFactory dlf = (DefinitionLabelFactory) PlatformUI.getWorkbench().getService(DefinitionLabelFactory.class);
		
		final Label inputAttributeLabel = new Label(configurationComposite, SWT.NONE);
		inputAttributeLabel.setText(Messages.NetworkExpansionFunctionWizardPage_1);
		Control inputAttributeText = dlf.createLabel(configurationComposite, getParent().getSourceItem().getDefinition(), false);
		inputAttributeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		final Label outputAttributeLabel = new Label(configurationComposite, SWT.NONE);
		outputAttributeLabel.setText(Messages.NetworkExpansionFunctionWizardPage_2);
		Control outputAttributeText = dlf.createLabel(configurationComposite, getParent().getTargetItem().getDefinition(), false);
		outputAttributeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		final Label expansionExpressionLabel = new Label(configurationComposite, SWT.NONE);
		expansionExpressionLabel.setText(Messages.NetworkExpansionFunctionWizardPage_3);
		
		// expression
		Set<String> variables = new TreeSet<String>();
		SchemaItem p = getParent().getSourceItem();
		for (SchemaItem var : p.getParent().getChildren()) {
			if (Number.class.isAssignableFrom(
					var.getPropertyType().getBinding()) 
					|| String.class.isAssignableFrom(
							var.getPropertyType().getBinding())) {
				variables.add(var.getName().getLocalPart());
			}
		}
		
		this.expressionEditor = new MathExpressionFieldEditor(
				"expression", "=", configurationComposite, variables); //$NON-NLS-1$ //$NON-NLS-2$
		if (this.initialExpression != null && !this.initialExpression.equals("")) { //$NON-NLS-1$
			this.expressionEditor.setStringValue(this.initialExpression);
		}
		else {
			this.expressionEditor.setStringValue("50"); //$NON-NLS-1$
		}
		
		this.expressionEditor.setEmptyStringAllowed(false);
		this.expressionEditor.setPage(this);
		this.expressionEditor.setPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID)) {
					update();
				}
			}
		});
		
		// re-set the layout because the field editor breaks it and sets its own
		configurationComposite.setLayout(fileSelectionLayout);
		configurationComposite.layout(true, true);
	}
	
	private void update() {
		setPageComplete(expressionEditor.isValid());
	}
	
	/**
	 * @see WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return this.expressionEditor != null 
						&& this.expressionEditor.getStringValue() != null;
	}

	/**
	 * @return the expansion expression
	 */
	public String getExpansion() {
		return this.expressionEditor.getStringValue();
	}
	
	/**
	 * Set the initial expression
	 * 
	 * @param value the expression to set
	 */
	public void setInitialExpression(String value) {
		this.initialExpression = value;
	}

}