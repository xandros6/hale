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

package eu.esdihumboldt.hale.common.core.io;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import eu.esdihumboldt.hale.common.core.io.report.IOReport;
import eu.esdihumboldt.hale.common.core.io.report.IOReporter;

/**
 * Interface for I/O providers
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @since 2.2
 */
public interface IOProvider {
	
	/**
	 * The configuration parameter name for the content type 
	 */
	public static final String PARAM_CONTENT_TYPE = "contentType";
	
	/**
	 * Execute the I/O provider.
	 * 
	 * @param progress the progress indicator, may be <code>null</code>
	 * @return the execution report
	 *  
	 * @throws IOProviderConfigurationException if the I/O provider was not
	 *   configured properly 
	 * @throws IOException if an I/O operation fails
	 */
	public IOReport execute(ProgressIndicator progress) throws IOProviderConfigurationException, IOException;
	
	/**
	 * Create a reporter configured for the execution of this I/O provider.
	 * 
	 * This method can also be used internally in the implementation of 
	 * {@link #execute(ProgressIndicator)}.
	 * 
	 * @return the I/O reporter
	 */
	public IOReporter createReporter();
	
	/**
	 * States if the execution of the provider is cancelable
	 * 
	 * @return if the execution is cancelable
	 */
	public boolean isCancelable();
	
	/**
	 * Validate the I/O provider configuration
	 * 
	 * @throws IOProviderConfigurationException if the I/O provider was not
	 *   configured properly
	 */
	public void validate() throws IOProviderConfigurationException;
	
	/**
	 * Set the content type. This may be optional if the I/O provider doesn't
	 * differentiate between content types.
	 * 
	 * @param contentType the content type
	 */
	public void setContentType(ContentType contentType);
	
	/**
	 * Get the content type
	 * 
	 * @return the content type, may be <code>null</code>
	 */
	public ContentType getContentType();
	
	/**
	 * Get the supported configuration parameters.
	 * 
	 * @return the supported parameters  
	 */
	public Set<String> getSupportedParameters();
	
	/**
	 * Set a parameter
	 * 
	 * @param name the parameter name
	 * @param value the parameter value
	 */
	public void setParameter(String name, String value);
	
	/**
	 * Get the value for the given parameter name 
	 * 
	 * @param name the parameter name
	 * @return the parameter value or <code>null</code>
	 */
	public String getParameter(String name);
	
	/**
	 * Load the configuration from a map of key/value pairs
	 * 
	 * @param configuration the configuration to load
	 */
	public void loadConfiguration(Map<String, String> configuration);
	
	/**
	 * Store the configuration in a map of key/value pairs
	 * 
	 * @param configuration the configuration to populate
	 */
	public void storeConfiguration(Map<String, String> configuration);
	
}