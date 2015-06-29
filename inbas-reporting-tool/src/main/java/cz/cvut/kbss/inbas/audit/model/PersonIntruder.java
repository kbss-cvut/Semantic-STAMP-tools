package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.PersonIntruder)
public class PersonIntruder {

    public static final String INTRUDER_TYPE = "person";

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_personCategory)
    private String category;

    @OWLDataProperty(iri = Vocabulary.p_activityDescription)
    private String whatWasDoing;

    @OWLObjectProperty(iri = Vocabulary.p_memberOf, cascade = CascadeType.PERSIST)
    private Organization organization;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWhatWasDoing() {
        return whatWasDoing;
    }

    public void setWhatWasDoing(String whatWasDoing) {
        this.whatWasDoing = whatWasDoing;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
