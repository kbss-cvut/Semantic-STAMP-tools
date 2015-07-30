package cz.cvut.kbss.inbas.audit.rest.dto.model;

/**
 * @author ledvima1
 */
public class GeneralEvent extends EventTypeAssessment {

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeneralEvent() {
    }

    public GeneralEvent(cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment assessment) {
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
