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

package eu.esdihumboldt.hale.align.extension.function;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.eclipse.core.runtime.IConfigurationElement;

import eu.esdihumboldt.hale.align.extension.function.internal.AbstractFunction;

/**
 * Property function
 * @author Simon Templer
 */
@Immutable
public final class PropertyFunction extends AbstractFunction {
	
	private final Set<PropertyParameter> source;
	private final Set<PropertyParameter> target;

	/**
	 * @see AbstractFunction#AbstractFunction(IConfigurationElement)
	 */
	public PropertyFunction(IConfigurationElement conf) {
		super(conf);
		
		// populate source and target properties
		source = new LinkedHashSet<PropertyParameter>();
		addProperties(source, conf.getChildren("sourceProperties"));
		
		target = new LinkedHashSet<PropertyParameter>();
		addProperties(target, conf.getChildren("targetProperties"));
	}
	
	private static void addProperties(Set<PropertyParameter> collector,
			IConfigurationElement[] propertiesElements) {
		if (propertiesElements != null) {
			for (IConfigurationElement propertiesElement : propertiesElements) {
				IConfigurationElement[] properties = propertiesElement.getChildren("property");
				if (properties != null) {
					for (IConfigurationElement property : properties) {
						collector.add(new PropertyParameter(property));
					}
				}
			}
		}
	}

	/**
	 * Get the source properties
	 * @return the source properties
	 */
	public Set<PropertyParameter> getSourceProperties() {
		return Collections.unmodifiableSet(source);
	}
	
	/**
	 * Get the target properties
	 * @return the target properties
	 */
	public Set<PropertyParameter> getTargetProperties() {
		return Collections.unmodifiableSet(target);
	}

}