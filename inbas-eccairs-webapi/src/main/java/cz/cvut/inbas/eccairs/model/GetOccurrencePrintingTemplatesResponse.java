
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
 *         &lt;element name="GetOccurrencePrintingTemplatesResult" type="{http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API}ewaResult" minOccurs="0"/>
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
    "getOccurrencePrintingTemplatesResult"
})
@XmlRootElement(name = "GetOccurrencePrintingTemplatesResponse")
public class GetOccurrencePrintingTemplatesResponse {

    @XmlElementRef(name = "GetOccurrencePrintingTemplatesResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<EwaResult> getOccurrencePrintingTemplatesResult;

    /**
     * Gets the value of the getOccurrencePrintingTemplatesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public JAXBElement<EwaResult> getGetOccurrencePrintingTemplatesResult() {
        return getOccurrencePrintingTemplatesResult;
    }

    /**
     * Sets the value of the getOccurrencePrintingTemplatesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public void setGetOccurrencePrintingTemplatesResult(JAXBElement<EwaResult> value) {
        this.getOccurrencePrintingTemplatesResult = value;
    }

}
