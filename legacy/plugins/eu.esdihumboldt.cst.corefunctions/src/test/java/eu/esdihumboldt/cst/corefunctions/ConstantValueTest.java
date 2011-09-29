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

package eu.esdihumboldt.cst.corefunctions;

import static org.junit.Assert.assertTrue;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;

import eu.esdihumboldt.commons.goml.align.Cell;
import eu.esdihumboldt.commons.goml.oml.ext.Parameter;
import eu.esdihumboldt.commons.goml.oml.ext.Transformation;
import eu.esdihumboldt.commons.goml.omwg.Property;
import eu.esdihumboldt.commons.goml.rdf.About;
import eu.esdihumboldt.commons.goml.rdf.Resource;

public class ConstantValueTest {
	/**
	 * Test for CST Function to set default
	 * attribute target values.
	 *
	 * @author Ulrich Schaeffler
	 * @partner Technische Universitaet Muenchen
	 * @version $Id$ 
	 */

	
	private final String targetLocalname = "FT2"; //$NON-NLS-1$
	private final String targetLocalnamePropertyBConst = "PropertyBConst"; //$NON-NLS-1$
	private final String targetNamespace = "http://xsdi.org"; //$NON-NLS-1$
	
	
	

	@Test
	public void testTransformFeatureFeatureString() {

		// set up cell to use for testing
		Cell cell = new Cell();
	
		Transformation t = new Transformation();
		t.setService(new Resource(ConstantValueFunction.class.toString()));
		t.getParameters().add(new Parameter("defaultValue", "Spingfield")); //$NON-NLS-1$ //$NON-NLS-2$
		
		Property p1 = new Property(new About(this.targetNamespace, this.targetLocalname, this.targetLocalnamePropertyBConst));
		p1.setTransformation(t);
		cell.setEntity1(null);
		cell.setEntity2(p1);

		// build source and target Features
		
		SimpleFeatureType targettype = this.getFeatureType(
				this.targetNamespace, 
				this.targetLocalname, 
				new String[]{this.targetLocalnamePropertyBConst});
		
		Feature target = SimpleFeatureBuilder.build(
				targettype, new Object[]{"City"}, "1"); //$NON-NLS-1$ //$NON-NLS-2$
		// perform actual test
		
		ConstantValueFunction constFunc = new ConstantValueFunction();
		constFunc.configure(cell);

		Feature neu = constFunc.transform(null, target);
		
		assertTrue(neu.getProperty(
				this.targetLocalnamePropertyBConst).getValue().toString().equals("Spingfield")); //$NON-NLS-1$
	
	}
	
	
	
	
	private SimpleFeatureType getFeatureType(String featureTypeNamespace, 
			String featureTypeName, String[] propertyNames) {
	
		SimpleFeatureType ft = null;
		try {
			SimpleFeatureTypeBuilder ftbuilder = new SimpleFeatureTypeBuilder();
			ftbuilder.setName(featureTypeName);
			ftbuilder.setNamespaceURI(featureTypeNamespace);
			for (String s : propertyNames) {
				ftbuilder.add(s, String.class);
			}
			ft = ftbuilder.buildFeatureType();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return ft;
	}
	
}