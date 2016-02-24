
package cz.cvut.inbas.eccairs.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetOccurrenceAsPDFResult" type="{http://schemas.microsoft.com/Message}StreamBody"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getOccurrenceAsPDFResult"
})
@XmlRootElement(name = "GetOccurrenceAsPDFResponse")
public class GetOccurrenceAsPDFResponse {

    @XmlElement(name = "GetOccurrenceAsPDFResult", required = true)
    protected byte[] getOccurrenceAsPDFResult;

    /**
     * Gets the value of the getOccurrenceAsPDFResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetOccurrenceAsPDFResult() {
        return getOccurrenceAsPDFResult;
    }

    /**
     * Sets the value of the getOccurrenceAsPDFResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetOccurrenceAsPDFResult(byte[] value) {
        this.getOccurrenceAsPDFResult = value;
    }

}
