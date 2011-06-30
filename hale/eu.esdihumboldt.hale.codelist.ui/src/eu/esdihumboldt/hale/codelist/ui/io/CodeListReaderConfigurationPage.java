package eu.esdihumboldt.hale.codelist.ui.io;

import org.eclipse.jface.resource.ImageDescriptor;
import eu.esdihumboldt.hale.codelist.io.CodeListReader;
import eu.esdihumboldt.hale.codelist.io.CodeListReaderFactory;
import eu.esdihumboldt.hale.ui.io.config.AbstractConfigurationPage;


/**
 * Configuration page for the code list reader
 * @author Patrick Lieb
 */
public abstract class CodeListReaderConfigurationPage extends AbstractConfigurationPage<CodeListReader, CodeListReaderFactory, 
CodeListImportWizard> {

	/**
	 * @see AbstractConfigurationPage#AbstractConfigurationPage(String, String, ImageDescriptor)
	 */
	protected CodeListReaderConfigurationPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * @see AbstractConfigurationPage#AbstractConfigurationPage(String)
	 */
	protected CodeListReaderConfigurationPage(String pageName) {
		super(pageName);
	}

}
