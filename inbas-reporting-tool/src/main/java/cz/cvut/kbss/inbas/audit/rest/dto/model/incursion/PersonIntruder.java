package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

/**
 * @author ledvima1
 */
public class PersonIntruder extends RunwayIntruder {

    private String personCategory;

    private String personOrganization;

    private String wasDoing;

    public PersonIntruder() {
    }

    public PersonIntruder(cz.cvut.kbss.inbas.audit.model.PersonIntruder intruder) {
        super(intruder.getUri(), cz.cvut.kbss.inbas.audit.model.PersonIntruder.INTRUDER_TYPE);
        this.personCategory = intruder.getCategory();
        this.personOrganization = intruder.getOrganization() != null ? intruder.getOrganization().getName() : null;
        this.wasDoing = intruder.getWhatWasDoing();
    }

    public String getPersonCategory() {
        return personCategory;
    }

    public void setPersonCategory(String personCategory) {
        this.personCategory = personCategory;
    }

    public String getPersonOrganization() {
        return personOrganization;
    }

    public void setPersonOrganization(String personOrganization) {
        this.personOrganization = personOrganization;
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
                "personCategory='" + personCategory + '\'' +
                ", organization='" + personOrganization + '\'' +
                "} " + super.toString();
    }
}
