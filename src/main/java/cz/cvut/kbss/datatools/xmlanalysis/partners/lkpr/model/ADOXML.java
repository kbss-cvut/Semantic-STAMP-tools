package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlKey;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "ADOXML")
@XmlAccessorType(XmlAccessType.FIELD)
public class ADOXML implements BaseEntity{
    // Mappings and fields
    @XmlKey
    @XmlAttribute
    protected String date;

    @XmlKey
    @XmlAttribute
    protected String time;

    @XmlKey
    @XmlAttribute
    protected String username;

    @XmlPath("MODELS/MODEL[@modeltype='Business process model']")
    protected List<BusinessProcessModel> businessProcessModels;

    @XmlPath("MODELS/MODEL[@modeltype='Risk pool']")
    protected List<RiskPool> riskPoolModels;

    @XmlPath("MODELS/MODEL[@modeltype='Working environment model']")
    protected List<OrganizationalModel> organizationalModels;

    // Getters and Setters
    public List<RiskPool> getRiskPoolModels() {
        return riskPoolModels;
    }

    public void setRiskPoolModels(List<RiskPool> riskPoolModels) {
        this.riskPoolModels = riskPoolModels;
    }

    public List<BusinessProcessModel> getBusinessProcessModels() {
        return businessProcessModels;
    }

    public void setBusinessProcessModels(List<BusinessProcessModel> businessProcessModels) {
        this.businessProcessModels = businessProcessModels;
    }

    public List<OrganizationalModel> getOrganizationalModels() {
        return organizationalModels;
    }

    public void setOrganizationalModels(List<OrganizationalModel> organizationalModels) {
        this.organizationalModels = organizationalModels;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




}