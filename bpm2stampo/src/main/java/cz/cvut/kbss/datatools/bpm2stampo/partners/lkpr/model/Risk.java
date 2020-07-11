package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Risk extends Instance {

    @XmlPath("ATTRIBUTE[@name='Likelihood']")
    protected String likelihood;
    @XmlPath("ATTRIBUTE[@name='Impact']")
    protected String impact;
    @XmlPath("ATTRIBUTE[@name='Risk type']")
    protected String riskType;


    public String getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(String likelihood) {
        this.likelihood = likelihood;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }
}
