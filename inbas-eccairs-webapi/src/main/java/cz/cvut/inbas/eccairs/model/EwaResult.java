
package cz.cvut.inbas.eccairs.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ewaResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ewaResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ErrorDetails" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReturnCode" type="{http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API}ResultReturnCode" minOccurs="0"/>
 *         &lt;element name="UserToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ewaResult", namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", propOrder = {
    "data",
    "errorDetails",
    "returnCode",
    "userToken"
})
public class EwaResult {

    @XmlElementRef(name = "Data", namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", type = JAXBElement.class, required = false)
    protected JAXBElement<String> data;
    @XmlElementRef(name = "ErrorDetails", namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", type = JAXBElement.class, required = false)
    protected JAXBElement<String> errorDetails;
    @XmlElement(name = "ReturnCode")
    protected ResultReturnCode returnCode;
    @XmlElementRef(name = "UserToken", namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API", type = JAXBElement.class, required = false)
    protected JAXBElement<String> userToken;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setData(JAXBElement<String> value) {
        this.data = value;
    }

    /**
     * Gets the value of the errorDetails property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getErrorDetails() {
        return errorDetails;
    }

    /**
     * Sets the value of the errorDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setErrorDetails(JAXBElement<String> value) {
        this.errorDetails = value;
    }

    /**
     * Gets the value of the returnCode property.
     * 
     * @return
     *     possible object is
     *     {@link ResultReturnCode }
     *     
     */
    public ResultReturnCode getReturnCode() {
        return returnCode;
    }

    /**
     * Sets the value of the returnCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultReturnCode }
     *     
     */
    public void setReturnCode(ResultReturnCode value) {
        this.returnCode = value;
    }

    /**
     * Gets the value of the userToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUserToken() {
        return userToken;
    }

    /**
     * Sets the value of the userToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUserToken(JAXBElement<String> value) {
        this.userToken = value;
    }

}
