
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
 *         &lt;element name="GetAttributesResult" type="{http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API}ewaResult" minOccurs="0"/>
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
    "getAttributesResult"
})
@XmlRootElement(name = "GetAttributesResponse")
public class GetAttributesResponse {

    @XmlElementRef(name = "GetAttributesResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<EwaResult> getAttributesResult;

    /**
     * Gets the value of the getAttributesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public JAXBElement<EwaResult> getGetAttributesResult() {
        return getAttributesResult;
    }

    /**
     * Sets the value of the getAttributesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public void setGetAttributesResult(JAXBElement<EwaResult> value) {
        this.getAttributesResult = value;
    }

}
