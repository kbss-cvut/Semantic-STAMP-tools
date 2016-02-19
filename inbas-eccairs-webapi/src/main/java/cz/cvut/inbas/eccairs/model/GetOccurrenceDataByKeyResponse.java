
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
 *         &lt;element name="GetOccurrenceDataByKeyResult" type="{http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API}ewaResult" minOccurs="0"/>
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
    "getOccurrenceDataByKeyResult"
})
@XmlRootElement(name = "GetOccurrenceDataByKeyResponse")
public class GetOccurrenceDataByKeyResponse {

    @XmlElementRef(name = "GetOccurrenceDataByKeyResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<EwaResult> getOccurrenceDataByKeyResult;

    /**
     * Gets the value of the getOccurrenceDataByKeyResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public JAXBElement<EwaResult> getGetOccurrenceDataByKeyResult() {
        return getOccurrenceDataByKeyResult;
    }

    /**
     * Sets the value of the getOccurrenceDataByKeyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public void setGetOccurrenceDataByKeyResult(JAXBElement<EwaResult> value) {
        this.getOccurrenceDataByKeyResult = value;
    }

}
