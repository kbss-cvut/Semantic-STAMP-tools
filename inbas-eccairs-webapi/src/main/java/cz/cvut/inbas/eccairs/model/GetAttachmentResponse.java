
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
 *         &lt;element name="GetAttachmentResult" type="{http://schemas.microsoft.com/Message}StreamBody"/>
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
    "getAttachmentResult"
})
@XmlRootElement(name = "GetAttachmentResponse")
public class GetAttachmentResponse {

    @XmlElement(name = "GetAttachmentResult", required = true)
    protected byte[] getAttachmentResult;

    /**
     * Gets the value of the getAttachmentResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetAttachmentResult() {
        return getAttachmentResult;
    }

    /**
     * Sets the value of the getAttachmentResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetAttachmentResult(byte[] value) {
        this.getAttachmentResult = value;
    }

}
