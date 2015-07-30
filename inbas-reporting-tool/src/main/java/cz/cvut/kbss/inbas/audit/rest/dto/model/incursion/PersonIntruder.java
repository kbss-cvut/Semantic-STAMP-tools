package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

/**
 * @author ledvima1
 */
public class PersonIntruder extends RunwayIntruder {

    private String category;

    private String organization;

    private String wasDoing;

    public PersonIntruder() {
    }

    public PersonIntruder(cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder intruder) {
        super(intruder.getUri(), cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder.INTRUDER_TYPE);
        this.category = intruder.getCategory();
        this.organization = intruder.getOrganization() != null ? intruder.getOrganization().getName() : null;
        this.wasDoing = intruder.getWhatWasDoing();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getWasDoing() {
        return wasDoing;
    }

    public void setWasDoing(String wasDoing) {
        this.wasDoing = wasDoing;
    }

    @Override
    public String toString() {
        return "PersonIntruder{" +
                "category='" + category + '\'' +
                ", organization='" + organization + '\'' +
                "} " + super.toString();
    }
}
