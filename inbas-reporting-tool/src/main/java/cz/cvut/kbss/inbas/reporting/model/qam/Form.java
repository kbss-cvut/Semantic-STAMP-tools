package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.Form)
public class Form implements HasQuestions, Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLObjectProperty(iri = Vocabulary.hasRelatedQuestion, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Question> subQuestions;

    public Form() {
    }

    public Form(Form other) {
        if (other.subQuestions != null) {
            this.subQuestions = other.subQuestions.stream().map(Question::new).collect(Collectors.toSet());
        }
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public Set<Question> getSubQuestions() {
        if (subQuestions == null) {
            this.subQuestions = new HashSet<>();
        }
        return subQuestions;
    }

    @Override
    public void setSubQuestions(Set<Question> subQuestions) {
        this.subQuestions = subQuestions;
    }

    @Override
    public String toString() {
        return "Form{" +
                "subQuestions=" + subQuestions +
                '}';
    }
}
