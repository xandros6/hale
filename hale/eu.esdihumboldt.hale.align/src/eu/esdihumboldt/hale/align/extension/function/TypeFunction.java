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
 * Type function
 * @author Simon Templer
 */
@Immutable
public final class TypeFunction extends AbstractFunction {

	private final Set<TypeParameter> source;
	private final Set<TypeParameter> target;

	/**
	 * @see AbstractFunction#AbstractFunction(IConfigurationElement)
	 */
	public TypeFunction(IConfigurationElement conf) {
		super(conf);
		
		// populate source and target properties
		source = new LinkedHashSet<TypeParameter>();
		addTypes(source, conf.getChildren("sourceTypes"));
		
		target = new LinkedHashSet<TypeParameter>();
		addTypes(target, conf.getChildren("targetTypes"));
	}
	
	private static void addTypes(Set<TypeParameter> collector,
			IConfigurationElement[] typesElements) {
		if (typesElements != null) {
			for (IConfigurationElement typesElement : typesElements) {
				IConfigurationElement[] types = typesElement.getChildren("type");
				if (types != null) {
					for (IConfigurationElement type : types) {
						collector.add(new TypeParameter(type));
					}
				}
			}
		}
	}

	/**
	 * Get the source properties
	 * @return the source properties
	 */
	public Set<TypeParameter> getSourceTypes() {
		return Collections.unmodifiableSet(source);
	}
	
	/**
	 * Get the target properties
	 * @return the target properties
	 */
	public Set<TypeParameter> getTargetTypes() {
		return Collections.unmodifiableSet(target);
	}

}
