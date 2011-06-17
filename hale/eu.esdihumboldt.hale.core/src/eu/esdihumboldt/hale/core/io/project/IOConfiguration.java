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

package eu.esdihumboldt.hale.core.io.project;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.esdihumboldt.hale.core.io.IOProvider;

/**
 * Object holding all information necessary to reproduce an {@link IOProvider}
 * execution, e.g. when loading a project.
 * @author Simon Templer
 */
public class IOConfiguration {
	
	private String advisorId;
	
	private String providerId;
	
	private final Map<String, String> providerConfiguration = new HashMap<String, String>();
	
	private final Set<String> dependencies = new HashSet<String>();
	
	private URI location;

	/**
	 * @return the advisorId
	 */
	public String getAdvisorId() {
		return advisorId;
	}

	/**
	 * @param advisorId the advisorId to set
	 */
	public void setAdvisorId(String advisorId) {
		this.advisorId = advisorId;
	}

	/**
	 * @return the providerId
	 */
	public String getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId the providerId to set
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	/**
	 * @return the location
	 */
	public URI getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(URI location) {
		this.location = location;
	}

	/**
	 * @return the providerConfiguration
	 */
	public Map<String, String> getProviderConfiguration() {
		return providerConfiguration;
	}

	/**
	 * @return the dependencies
	 */
	public Set<String> getDependencies() {
		return dependencies;
	}

}
