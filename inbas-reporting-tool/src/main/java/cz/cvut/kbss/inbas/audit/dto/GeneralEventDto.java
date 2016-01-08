package cz.cvut.kbss.inbas.audit.dto;

public class GeneralEventDto extends EventTypeAssessmentDto {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeneralEventDto() {
    }

    public GeneralEventDto(cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment assessment) {
        super(assessment.getUri());
        this.eventType = assessment.getEventType();
        this.description = assessment.getDescription();
    }

    @Override
    public String toString() {
        return "GeneralEvent{" +
                "eventType=" + getEventType() +
                "description='" + description + '\'' +
                "}";
    }
}
