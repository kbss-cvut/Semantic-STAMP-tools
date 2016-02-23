
package cz.cvut.inbas.eccairs.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="GetAttachmentsDetailsByKeyResult" type="{http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API}ewaResult" minOccurs="0"/>
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
    "getAttachmentsDetailsByKeyResult"
})
@XmlRootElement(name = "GetAttachmentsDetailsByKeyResponse")
public class GetAttachmentsDetailsByKeyResponse {

    @XmlElementRef(name = "GetAttachmentsDetailsByKeyResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<EwaResult> getAttachmentsDetailsByKeyResult;

    /**
     * Gets the value of the getAttachmentsDetailsByKeyResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public JAXBElement<EwaResult> getGetAttachmentsDetailsByKeyResult() {
        return getAttachmentsDetailsByKeyResult;
    }

    /**
     * Sets the value of the getAttachmentsDetailsByKeyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public void setGetAttachmentsDetailsByKeyResult(JAXBElement<EwaResult> value) {
        this.getAttachmentsDetailsByKeyResult = value;
    }

}
