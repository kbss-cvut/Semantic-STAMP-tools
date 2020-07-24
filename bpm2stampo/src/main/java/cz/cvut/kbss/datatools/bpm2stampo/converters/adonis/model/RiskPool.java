package cz.cvut.kbss.datatools.bpm2stampo.converters.adonis.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class RiskPool extends Model {
    @XmlPath("INSTANCE[contains('Risk', @class)]")
    protected List<Risk> risks;

    public List<Risk> getRisks() {
        return risks;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }
}
