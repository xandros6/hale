package eu.esdihumboldt.hale.codelist.io.xml.content;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.esdihumboldt.hale.core.io.tester.AbstractXmlTester;

/**
 * Testing class for {@link XmlCodeListContentTypeTester}
 * @author Patrick Lieb
 */
public class XmlCodeListContentTypeTestTester {

	/**
	 * Test loading xml code list
	 */
	@Test
	public void testTrueXmlCodeList(){
		AbstractXmlTester tester = new XmlCodeListContentTypeTester();
		
		assertTrue(tester.matchesContentType(getClass().getResourceAsStream("/data/AdministrativeHierarchyLevel.xml")));
	}
}
