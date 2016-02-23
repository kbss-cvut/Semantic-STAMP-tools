
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
 *         &lt;element name="LoginByInfoResult" type="{http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API}ewaResult" minOccurs="0"/>
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
    "loginByInfoResult"
})
@XmlRootElement(name = "LoginByInfoResponse")
public class LoginByInfoResponse {

    @XmlElementRef(name = "LoginByInfoResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<EwaResult> loginByInfoResult;

    /**
     * Gets the value of the loginByInfoResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public JAXBElement<EwaResult> getLoginByInfoResult() {
        return loginByInfoResult;
    }

    /**
     * Sets the value of the loginByInfoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EwaResult }{@code >}
     *     
     */
    public void setLoginByInfoResult(JAXBElement<EwaResult> value) {
        this.loginByInfoResult = value;
    }

}
