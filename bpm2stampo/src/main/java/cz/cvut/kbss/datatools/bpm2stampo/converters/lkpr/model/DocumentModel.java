package cz.cvut.kbss.datatools.bpm2stampo.converters.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentModel extends Model{

    @XmlPath("INSTANCE[@class='Document']")
    protected List<Instance> documents;

    public List<Instance> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Instance> documents) {
        this.documents = documents;
    }
}
