//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.01 at 08:57:42 AM MEZ 
//


package eu.esdihumboldt.hale.server.war.wps;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * A combination of format, encoding, and/or schema supported by a process input or output. 
 * 
 * <p>Java class for ComplexDataDescriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ComplexDataDescriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MimeType" type="{http://www.opengis.net/ows/1.1}MimeType"/>
 *         &lt;element name="Encoding" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="Schema" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexDataDescriptionType", namespace = "http://www.opengis.net/wps/1.0.0", propOrder = {
    "mimeType",
    "encoding",
    "schema"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class ComplexDataDescriptionType {

    @XmlElement(name = "MimeType", namespace = "", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String mimeType;
    @XmlElement(name = "Encoding", namespace = "")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String encoding;
    @XmlElement(name = "Schema", namespace = "")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String schema;

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the encoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the value of the encoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Gets the value of the schema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2011-12-01T08:57:42+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setSchema(String value) {
        this.schema = value;
    }

}