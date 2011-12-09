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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;

import eu.esdihumboldt.commons.goml.align.Cell;
import eu.esdihumboldt.commons.goml.oml.ext.Parameter;
import eu.esdihumboldt.commons.goml.oml.ext.Transformation;
import eu.esdihumboldt.commons.goml.omwg.ComposedProperty;
import eu.esdihumboldt.commons.goml.omwg.Property;
import eu.esdihumboldt.commons.goml.rdf.About;
import eu.esdihumboldt.commons.goml.rdf.Resource;
import eu.esdihumboldt.specification.cst.align.ext.IParameter;


/**
 * Concatenation test-class
 * @author Stefan Gessner
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class ConcatenationOfAttributesFunctionTest {
	
	/**
	 * The name of the parameter for the separator.
	 */
	public static final String SEPERATOR = "seperator"; //$NON-NLS-1$
	
	/**
	 * The name of the parameter for the concatenation.
	 */
	public static final String CONCATENATION = "concatenation"; //$NON-NLS-1$

	/**
	 * 
	 */
	private final String sourceLocalname = "waterVA/Watercourses_VA_Type"; //$NON-NLS-1$
	/**
	 * 
	 */
	private final String sourceLocalnamePropertyDouble = "LAENGE_ARC"; //$NON-NLS-1$
	/**
	 * 
	 */
	private final String sourceNamespace = "http://esdi-humboldt.org"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final String source2Localname = "waterVA/Watercourses_VA_Type"; //$NON-NLS-1$
	/**
	 * 
	 */
	private final String source2LocalnamePropertyDouble = "LAENGE_ROU"; //$NON-NLS-1$
	/**
	 * 
	 */
	private final String source2Namespace = "http://esdi-humboldt.org"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private final String targetLocalname = "FT3"; //$NON-NLS-1$
	/**
	 * 
	 */
	private final String targetLocalnamePropertyString = "PropertyString"; //$NON-NLS-1$
	/**
	 * 
	 */
	private final String targetNamespace = "http://esdi-humboldt.eu"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static double x = 23.3456;
	/**
	 * 
	 */
	private static double y = 14.3465;
	
	/**
	 * 
	 */
	private static float x2 = (float) 23.3465;
	/**
	 * 
	 */
	private static float y2 = (float) 14.3456;
	
	/**
	 * 
	 */
	private static int x3 = 23;
	
	/**
	 * 
	 */
	private static int y3 = 14;

	/**
	 * 
	 */
	private int testInt = 1;

	/**
	 * 
	 */
	@Test
	public void testConfigure() {
		ConcatenationOfAttributesFunction  coaf = new ConcatenationOfAttributesFunction();
		coaf.configure(coaf.getParameters());
	}
	
	/**
	 * 
	 */
	@Test
	public void testConcatenationOfAttributesFunction() {

		for(this.testInt =1; this.testInt<7; this.testInt++){
			// set up cell to use for testing
			Cell cell = new Cell();
	
			Transformation t = new Transformation();
			t.setService(new Resource(ConcatenationOfAttributesFunction.class.getName()));
			List<IParameter> parameters = new ArrayList<IParameter>();
			parameters.add(new Parameter("seperator", ";")); //$NON-NLS-1$ //$NON-NLS-2$
			parameters.add(new Parameter("concatenation", "LAENGE_ROU--!-split-!--laskjdflk--!-split-!--LAENGE_ARC--!-split-!--aölskdjf")); //$NON-NLS-1$ //$NON-NLS-2$
			t.setParameters(parameters);
			
			ComposedProperty composedProperty = new ComposedProperty(new About("")); //$NON-NLS-1$
			composedProperty.setTransformation(t);
			
			Property entity1 = new Property(new About(this.sourceNamespace, this.sourceLocalname, this.sourceLocalnamePropertyDouble));
			Property entity2 = new Property(new About(this.source2Namespace, this.source2Localname, this.source2LocalnamePropertyDouble));
			Property entity3 = new Property(new About(this.targetNamespace, this.targetLocalname, this.targetLocalnamePropertyString));
	
			composedProperty.getCollection().add(entity1);
			composedProperty.getCollection().add(entity2);
			
			cell.setEntity1(composedProperty);
			cell.setEntity2(entity3);
			
			// build source Features
			SimpleFeatureType sourcetype = this.getFeatureType(this.sourceNamespace, this.sourceLocalname, new String[]{this.sourceLocalnamePropertyDouble, this.source2LocalnamePropertyDouble});
			SimpleFeatureType targettype = this.getFeatureType(this.targetNamespace, this.targetLocalname, new String[]{this.targetLocalnamePropertyString});			
			
			Feature source = null;
			Feature target = null;
	
			switch(this.testInt){
			case 1:
				source = (Feature) SimpleFeatureBuilder.build(sourcetype, new Object[] {new Double(x),new Double(y)}, "1"); //$NON-NLS-1$
				target = (Feature) SimpleFeatureBuilder.build(targettype, new Object[] {}, "2"); //$NON-NLS-1$
				break;
			case 2:
				source = (Feature) SimpleFeatureBuilder.build(sourcetype, new Object[] {new Integer(x3),new Integer(y3)}, "1"); //$NON-NLS-1$
				target = (Feature) SimpleFeatureBuilder.build(targettype, new Object[] {}, "2"); //$NON-NLS-1$
				break;
			case 3:
				source = (Feature) SimpleFeatureBuilder.build(sourcetype, new Object[] {new Float(x2),new Float(y2)}, "1"); //$NON-NLS-1$
				target = (Feature) SimpleFeatureBuilder.build(targettype, new Object[] {}, "2"); //$NON-NLS-1$
				break;
			case 4:
				source = (Feature) SimpleFeatureBuilder.build(sourcetype, new Object[] {new Double(x),new Integer(y3)}, "1"); //$NON-NLS-1$
				target = (Feature) SimpleFeatureBuilder.build(targettype, new Object[] {}, "2"); //$NON-NLS-1$
				break;
			case 5:
				source = (Feature) SimpleFeatureBuilder.build(sourcetype, new Object[] {new Double(x),new Float(y2)}, "1"); //$NON-NLS-1$
				target = (Feature) SimpleFeatureBuilder.build(targettype, new Object[] {}, "2"); //$NON-NLS-1$
				break;
			case 6:
				source = (Feature) SimpleFeatureBuilder.build(sourcetype, new Object[] {new Integer(x3),new Float(y2)}, "1"); //$NON-NLS-1$
				target = (Feature) SimpleFeatureBuilder.build(targettype, new Object[] {}, "2"); //$NON-NLS-1$
				break;
			}
			
			// perform actual test
			
			Transformation oldT = (Transformation) cell.getEntity1().getTransformation();
			String oldConcatenation = oldT.getParameterMap().get(CONCATENATION).getValue();
			String oldSeperator = oldT.getParameterMap().get(SEPERATOR).getValue();
			
			ConcatenationOfAttributesFunction test = new ConcatenationOfAttributesFunction();
			test.configure(cell);
			test.transform(source, target);
			
			String transformedConcatenation = target.getProperty(this.targetLocalnamePropertyString).getValue().toString();
			
			String[] concat = oldConcatenation.split("--!-split-!--"); //$NON-NLS-1$
			String finalConcatString = ""; //$NON-NLS-1$
			for (String thisElement : concat) {
				org.opengis.feature.Property p = source.getProperty(thisElement);
				if (finalConcatString.length() > 0) {
					finalConcatString += oldSeperator;
				}
				
				if (p != null) {
					if (p.getValue() != null) {
						finalConcatString += p.getValue().toString();
					}
					else {
						finalConcatString += ""; //$NON-NLS-1$
					}
				}
				else {
					finalConcatString += thisElement;
				}
			}
			
			assertTrue(transformedConcatenation.equals(finalConcatString));
		}
	}
	
	/**
	 * @param featureTypeNamespace
	 * @param featureTypeName
	 * @param propertyNames
	 * @return a SimpleFeaturetype
	 */
	private SimpleFeatureType getFeatureType(String featureTypeNamespace, 
			String featureTypeName, String[] propertyNames) {
	
		SimpleFeatureType ft = null;
		try {
			SimpleFeatureTypeBuilder ftbuilder = new SimpleFeatureTypeBuilder();
			ftbuilder.setName(featureTypeName);
			ftbuilder.setNamespaceURI(featureTypeNamespace);
			switch(this.testInt){
			case 1:
				for (String s : propertyNames) {
					if(propertyNames.length==1){
						ftbuilder.add(s, String.class);
					}
					else{
						ftbuilder.add(s, Double.class);
					}
				}
				break;
			case 2:
				for (String s : propertyNames) {
					if(propertyNames.length==1){
						ftbuilder.add(s, String.class);
					}
					else{
						ftbuilder.add(s, Integer.class);
					}
				}
			case 3:
				for (String s : propertyNames) {
					if(propertyNames.length==1){
						ftbuilder.add(s, String.class);
					}
					else{
						ftbuilder.add(s, Float.class);
					}
				}
				break;
			case 4:
				if(propertyNames.length==1){
					ftbuilder.add(propertyNames[0], String.class);
				}
				else{
					ftbuilder.add(propertyNames[0], Double.class);
					ftbuilder.add(propertyNames[1], Integer.class);
				}
				break;	
			case 5:
				if(propertyNames.length==1){
					ftbuilder.add(propertyNames[0], String.class);
				}
				else{
					ftbuilder.add(propertyNames[0], Double.class);
					ftbuilder.add(propertyNames[1], Float.class);
				}
				break;	
			case 6:
				if(propertyNames.length==1){
					ftbuilder.add(propertyNames[0], String.class);
				}
				else{
					ftbuilder.add(propertyNames[0], Integer.class);
					ftbuilder.add(propertyNames[1], Float.class);
				}
				break;	
			}
			ft = ftbuilder.buildFeatureType();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return ft;
	}		
}