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

package eu.esdihumboldt.hale.schema;

import java.util.Date;

import com.vividsolutions.jts.geom.Geometry;

import eu.esdihumboldt.hale.schema.geometry.GeometryProperty;
import eu.esdihumboldt.hale.schema.model.Definition;
import eu.esdihumboldt.hale.schema.model.GroupPropertyDefinition;
import eu.esdihumboldt.hale.schema.model.PropertyDefinition;
import eu.esdihumboldt.hale.schema.model.TypeDefinition;
import eu.esdihumboldt.hale.schema.model.constraint.type.AbstractFlag;
import eu.esdihumboldt.hale.schema.model.constraint.type.Binding;
import eu.esdihumboldt.hale.schema.model.constraint.type.SimpleFlag;

/**
 * Basic classification for definitions
 * @author Simon Templer
 * @since 2.2
 */
public enum Classification {
	/** Abstract feature type */
	ABSTRACT_FT,
	/** Concrete feature type */
	CONCRETE_FT,
	/** Property type */
	TYPE,
	/** Numeric property */
	NUMERIC_PROPERTY,
	/** String property */
	STRING_PROPERTY,
	/** Complex property */
	COMPLEX_PROPERTY,
	/** Geometric property */
	GEOMETRIC_PROPERTY,
	/** Group */
	GROUP,
	/** Unknown */
	UNKNOWN;
	
	/**
	 * Determine the classification for a definition
	 * @param def the definition
	 * @return the classification for the definition
	 */
	public static Classification getClassification(Definition<?> def) {
		if (def instanceof GroupPropertyDefinition) {
			return GROUP;
		}
		else if (def instanceof PropertyDefinition) {
			// use binding/constraints to determine type
			PropertyDefinition property = (PropertyDefinition) def;
			if (property.getPropertyType().getConstraint(SimpleFlag.class).isEnabled()) {
				// simple type
				Class<?> binding = property.getPropertyType().getConstraint(Binding.class).getBinding();
				if (Number.class.isAssignableFrom(binding) || 
						Date.class.isAssignableFrom(binding)) {
					return NUMERIC_PROPERTY;
				}
				else if (Geometry.class.isAssignableFrom(binding) ||
						GeometryProperty.class.isAssignableFrom(binding)) { // additional checks?
					return GEOMETRIC_PROPERTY;
				}
				
				// default to string for simple types
				return STRING_PROPERTY;
			}
			else {
				// complex type
				return COMPLEX_PROPERTY;
			}
		}
		else if (def instanceof TypeDefinition)  {
			TypeDefinition type = (TypeDefinition) def;
			if (isAbstractFeatureType(type)) {
				return ABSTRACT_FT;
			}
			else {
				TypeDefinition superType = type.getSuperType();
				while (superType != null) {
					if (isAbstractFeatureType(superType)) {
						return (type.getConstraint(AbstractFlag.class).isEnabled())?(ABSTRACT_FT):(CONCRETE_FT);
					}
					
					superType = superType.getSuperType();
				}
				
				return TYPE;
			}
		}
		
		return UNKNOWN;
	}

	/**
	 * Determines if a type is the AbstractFeatureType. This is related to GML.
	 * @param type the type definition
	 * @return if the type is the AbstractFeatureType
	 */
	private static boolean isAbstractFeatureType(TypeDefinition type) {
		//XXX not really nice
		return type.getName().getLocalPart().equals("AbstractFeatureType") &&
			type.getName().getNamespaceURI().startsWith("http://www.opengis.net/") &&
			type.getName().getNamespaceURI().contains("gml");
	}
}