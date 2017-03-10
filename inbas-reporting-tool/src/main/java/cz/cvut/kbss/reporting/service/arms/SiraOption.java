package cz.cvut.kbss.reporting.service.arms;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.net.URI;

/**
 * The natural ordering of instances of this class is given by their data value - {@link #getDataValue()}.
 */
@OWLClass(iri = "http://onto.fel.cvut.cz/ontologies/arms/sira/model/sira-option")
public class SiraOption implements Comparable<SiraOption> {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.s_p_has_data_value)
    private Double dataValue;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Double getDataValue() {
        return dataValue;
    }

    public void setDataValue(Double dataValue) {
        this.dataValue = dataValue;
    }

    @Override
    public int compareTo(SiraOption other) {
        return dataValue.compareTo(other.dataValue);
    }
}
