
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
 *         &lt;element name="GetOccurrenceAsPDFByKeyResult" type="{http://schemas.microsoft.com/Message}StreamBody"/>
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
    "getOccurrenceAsPDFByKeyResult"
})
@XmlRootElement(name = "GetOccurrenceAsPDFByKeyResponse")
public class GetOccurrenceAsPDFByKeyResponse {

    @XmlElement(name = "GetOccurrenceAsPDFByKeyResult", required = true)
    protected byte[] getOccurrenceAsPDFByKeyResult;

    /**
     * Gets the value of the getOccurrenceAsPDFByKeyResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetOccurrenceAsPDFByKeyResult() {
        return getOccurrenceAsPDFByKeyResult;
    }

    /**
     * Sets the value of the getOccurrenceAsPDFByKeyResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetOccurrenceAsPDFByKeyResult(byte[] value) {
        this.getOccurrenceAsPDFByKeyResult = value;
    }

}
