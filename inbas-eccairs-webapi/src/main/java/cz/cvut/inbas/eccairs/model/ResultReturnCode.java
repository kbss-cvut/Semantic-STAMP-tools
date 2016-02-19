
package cz.cvut.inbas.eccairs.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultReturnCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResultReturnCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *     &lt;enumeration value="Error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResultReturnCode", namespace = "http://schemas.datacontract.org/2004/07/ECCAIRS5.Repository.Web.API")
@XmlEnum
public enum ResultReturnCode {

    OK("OK"),
    @XmlEnumValue("Error")
    ERROR("Error");
    private final String value;

    ResultReturnCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResultReturnCode fromValue(String v) {
        for (ResultReturnCode c: ResultReturnCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
