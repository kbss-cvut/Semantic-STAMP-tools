package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

@OWLClass(iri = Vocabulary.Occurrence)
public class Occurrence implements HasOwlKey, Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_label)
    private String name;    // Simple name of the event being reported

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_reportingPhase)
    private ReportingPhase reportingPhase;

    public Occurrence() {
        this.reportingPhase = ReportingPhase.INITIAL;
    }

    public Occurrence(Occurrence other) {
        Objects.requireNonNull(other);
        this.name = other.name;
        this.reportingPhase = other.reportingPhase;
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
            // Note: this either has to be called before persist or a setter has to be called to make JOPA notice the change
            this.key = IdentificationUtils.generateKey();
        }
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReportingPhase getReportingPhase() {
        return reportingPhase;
    }

    /**
     * Setter for persistence purposes.
     * <p>
     * For phase transitions, use {@link #transitionToPhase(ReportingPhase)}.
     *
     * @param reportingPhase Phase to set
     */
    public void setReportingPhase(ReportingPhase reportingPhase) {
        this.reportingPhase = reportingPhase;
    }

    /**
     * Transitions to the specified target phase, if the occurrence is not already in a further phase.
     *
     * @param targetPhase Target reporting phase
     */
    public void transitionToPhase(ReportingPhase targetPhase) {
        assert targetPhase != null;
        if (reportingPhase == ReportingPhase.INITIAL) {
            this.reportingPhase = targetPhase;
        } else if (reportingPhase == ReportingPhase.PRELIMINARY && targetPhase != ReportingPhase.INITIAL) {
            this.reportingPhase = targetPhase;
        }
    }

    @Override
    public String toString() {
        return "Occurrence{" +
                "uri=" + uri +
                ", name='" + name + '\'' +
                ", reportingPhase=" + reportingPhase +
                '}';
    }
}
