
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
 *         &lt;element name="GetViewImageResult" type="{http://schemas.microsoft.com/Message}StreamBody"/>
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
    "getViewImageResult"
})
@XmlRootElement(name = "GetViewImageResponse")
public class GetViewImageResponse {

    @XmlElement(name = "GetViewImageResult", required = true)
    protected byte[] getViewImageResult;

    /**
     * Gets the value of the getViewImageResult property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getGetViewImageResult() {
        return getViewImageResult;
    }

    /**
     * Sets the value of the getViewImageResult property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setGetViewImageResult(byte[] value) {
        this.getViewImageResult = value;
    }

}
