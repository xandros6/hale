//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.08 at 04:07:50 PM MEZ 
//


package eu.esdihumboldt.hale.models.project.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InstanceData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InstanceData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="wkt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="epsgcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceData", propOrder = {
    "path",
    "wkt",
    "epsgcode"
})
public class InstanceData {

    @XmlElement(required = true)
    protected String path;
    protected String wkt;
    protected String epsgcode;

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the wkt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWkt() {
        return wkt;
    }

    /**
     * Sets the value of the wkt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWkt(String value) {
        this.wkt = value;
    }

    /**
     * Gets the value of the epsgcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEpsgcode() {
        return epsgcode;
    }

    /**
     * Sets the value of the epsgcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEpsgcode(String value) {
        this.epsgcode = value;
    }

}
