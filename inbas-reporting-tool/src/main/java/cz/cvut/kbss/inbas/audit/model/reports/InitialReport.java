package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.HasOwlKey;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.InitialReport)
public class InitialReport implements HasOwlKey, Serializable, Report {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String text;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @Types(fetchType = FetchType.EAGER)
    private Set<String> types;

    public InitialReport() {
    }

    public InitialReport(String text) {
        this.text = text;
    }

    /**
     * Copy constructor.
     * <p>
     * Uses only text of the other report, key and uri are not copied.
     *
     * @param other The other report
     */
    public InitialReport(InitialReport other) {
        assert other != null;
        this.text = other.text;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getTypes() {
        if (types == null) {
            this.types = new HashSet<>(2);
        }
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        Objects.requireNonNull(type);
        getTypes().add(type);
    }

    @Override
    public String toString() {
        return "InitialReport{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void generateKey() {
        if (key == null) {
            this.key = IdentificationUtils.generateKey();
        }
    }

    @Override
    public ReportingPhase getPhase() {
        return ReportingPhase.INITIAL;
    }
}
