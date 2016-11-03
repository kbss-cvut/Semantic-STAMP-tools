package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.Transient;

import java.net.URI;
import java.util.Objects;

@OWLClass(iri = Vocabulary.s_c_safety_issue_risk_assessment)
public class SafetyIssueRiskAssessment extends AbstractEntity {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_initial_event_frequency)
    private URI initialEventFrequency;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_barrier_uos_avoidance_fail_frequency)
    private URI barrierUosAvoidanceFailFrequency;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_barrier_recovery_fail_frequency)
    private URI barrierRecoveryFailFrequency;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_accident_severity)
    private URI accidentSeverity;

    @Transient
    private URI siraValue;

    public SafetyIssueRiskAssessment() {
    }

    public SafetyIssueRiskAssessment(SafetyIssueRiskAssessment other) {
        Objects.requireNonNull(other);
        this.initialEventFrequency = other.initialEventFrequency;
        this.barrierUosAvoidanceFailFrequency = other.barrierUosAvoidanceFailFrequency;
        this.barrierRecoveryFailFrequency = other.barrierRecoveryFailFrequency;
        this.accidentSeverity = other.accidentSeverity;
        this.siraValue = other.siraValue;
    }

    public URI getInitialEventFrequency() {
        return initialEventFrequency;
    }

    public void setInitialEventFrequency(URI initialEventFrequency) {
        this.initialEventFrequency = initialEventFrequency;
    }

    public URI getBarrierUosAvoidanceFailFrequency() {
        return barrierUosAvoidanceFailFrequency;
    }

    public void setBarrierUosAvoidanceFailFrequency(URI barrierUosAvoidanceFailFrequency) {
        this.barrierUosAvoidanceFailFrequency = barrierUosAvoidanceFailFrequency;
    }

    public URI getBarrierRecoveryFailFrequency() {
        return barrierRecoveryFailFrequency;
    }

    public void setBarrierRecoveryFailFrequency(URI barrierRecoveryFailFrequency) {
        this.barrierRecoveryFailFrequency = barrierRecoveryFailFrequency;
    }

    public URI getAccidentSeverity() {
        return accidentSeverity;
    }

    public void setAccidentSeverity(URI accidentSeverity) {
        this.accidentSeverity = accidentSeverity;
    }

    public URI getSiraValue() {
        return siraValue;
    }

    public void setSiraValue(URI siraValue) {
        this.siraValue = siraValue;
    }

    @Override
    public String toString() {
        return "SafetyIssueRiskAssessment{" +
                "initialEventFrequency=" + initialEventFrequency +
                ", barrierUosAvoidanceFailFrequency=" + barrierUosAvoidanceFailFrequency +
                ", barrierRecoveryFailFrequency=" + barrierRecoveryFailFrequency +
                ", accidentSeverity=" + accidentSeverity + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SafetyIssueRiskAssessment)) return false;

        SafetyIssueRiskAssessment that = (SafetyIssueRiskAssessment) o;

        if (initialEventFrequency != null ? !initialEventFrequency.equals(that.initialEventFrequency) :
            that.initialEventFrequency != null) return false;
        if (barrierUosAvoidanceFailFrequency != null ?
            !barrierUosAvoidanceFailFrequency.equals(that.barrierUosAvoidanceFailFrequency) :
            that.barrierUosAvoidanceFailFrequency != null) return false;
        if (barrierRecoveryFailFrequency != null ?
            !barrierRecoveryFailFrequency.equals(that.barrierRecoveryFailFrequency) :
            that.barrierRecoveryFailFrequency != null) return false;
        return accidentSeverity != null ? accidentSeverity.equals(that.accidentSeverity) :
               that.accidentSeverity == null;

    }

    @Override
    public int hashCode() {
        int result = initialEventFrequency != null ? initialEventFrequency.hashCode() : 0;
        result = 31 * result +
                (barrierUosAvoidanceFailFrequency != null ? barrierUosAvoidanceFailFrequency.hashCode() : 0);
        result = 31 * result + (barrierRecoveryFailFrequency != null ? barrierRecoveryFailFrequency.hashCode() : 0);
        result = 31 * result + (accidentSeverity != null ? accidentSeverity.hashCode() : 0);
        return result;
    }
}
