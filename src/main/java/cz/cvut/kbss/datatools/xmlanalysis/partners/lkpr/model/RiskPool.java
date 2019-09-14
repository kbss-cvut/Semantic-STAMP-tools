package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class RiskPool extends Model {
    @XmlPath("INSTANCE[contains('Risk', @class)]")
    protected List<Instance> risks;

    public List<Instance> getRisks() {
        return risks;
    }

    public void setRisks(List<Instance> risks) {
        this.risks = risks;
    }
}
