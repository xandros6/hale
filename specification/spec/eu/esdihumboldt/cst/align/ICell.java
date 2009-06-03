/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                  01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.cst.align;

import eu.esdihumboldt.cst.rdf.IAbout;

/**
 * A {@link ICell} contains a mapping between two {@link IEntity}s.
 * 
 * @author Thorsten Reitz 
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public interface ICell {
	
	/**
	 * @return the first {@link IEntity} of the {@link ICell}.
	 */
	public IEntity getEntity1();

	/**
	 * @return the second {@link IEntity} of the {@link ICell}.
	 */
	public IEntity getEntity2();
	
	/**
	 * @return the relation
	 */
	public RelationType getRelation();

	/**
	 * @return the measure
	 */
	public IMeasure getMeasure();

	/**
	 * @return the about
	 */
	public IAbout getAbout();


	public enum RelationType {
		Equivalence,
		Subsumes,
		SubsumedBy,
		InstanceOf,
		HasInstance,
		Disjoint,
		PartOf, // TODO, might have to go elsewhere. added by MdV
		Extra, // TODO, might have to go elsewhere. added by MdV
		Missing // TODO, might have to go elsewhere. added by MdV
	}

}
