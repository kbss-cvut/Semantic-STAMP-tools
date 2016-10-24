package cz.cvut.kbss.inbas.reporting.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.cvut.kbss.inbas.reporting.dto.event.FactorGraph;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.FormGenData;

import java.net.URI;
import java.util.Set;

// It is important that occurrence comes before factorGraph, because it defines a reference to the occurrence, which can the be used
@JsonPropertyOrder(value = {"uri, key, occurrence, factorGraph"})
public class OccurrenceReportDto extends AbstractReportDto implements FormGenData {

    private URI phase;

    private OccurrenceDto occurrence;

    private FactorGraph factorGraph;

    private URI severityAssessment;

    private Set<URI> responsibleDepartments;

    private Set<CorrectiveMeasureRequestDto> correctiveMeasures;

    // ARMS Attributes

    private URI accidentOutcome;

    private URI barrierEffectiveness;

    private Integer armsIndex;

    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    public OccurrenceDto getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(OccurrenceDto occurrence) {
        this.occurrence = occurrence;
    }

    public FactorGraph getFactorGraph() {
        return factorGraph;
    }

    public void setFactorGraph(FactorGraph factorGraph) {
        this.factorGraph = factorGraph;
    }

    public URI getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(URI severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<URI> getResponsibleDepartments() {
        return responsibleDepartments;
    }

    public void setResponsibleDepartments(Set<URI> responsibleDepartments) {
        this.responsibleDepartments = responsibleDepartments;
    }

    public Set<CorrectiveMeasureRequestDto> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequestDto> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public URI getAccidentOutcome() {
        return accidentOutcome;
    }

    public void setAccidentOutcome(URI accidentOutcome) {
        this.accidentOutcome = accidentOutcome;
    }

    public URI getBarrierEffectiveness() {
        return barrierEffectiveness;
    }

    public void setBarrierEffectiveness(URI barrierEffectiveness) {
        this.barrierEffectiveness = barrierEffectiveness;
    }

    public Integer getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Integer armsIndex) {
        this.armsIndex = armsIndex;
    }

    @Override
    public ReportDto toReportDto() {
        final cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto res = new cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto();
        copyAttributes(res);
        res.setPhase(phase);
        res.getTypes().add(Vocabulary.s_c_occurrence_report);
        assert occurrence != null;
        res.setIdentification(occurrence.getName());
        res.setDate(occurrence.getStartTime());
        res.setSummary(getSummary());
        res.setSeverityAssessment(severityAssessment);
        res.setOccurrenceCategories(occurrence.getEventTypes());
        return res;
    }
}
