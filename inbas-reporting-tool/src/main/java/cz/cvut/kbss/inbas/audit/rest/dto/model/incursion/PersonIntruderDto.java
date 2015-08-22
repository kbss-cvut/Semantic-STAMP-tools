package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

/**
 * @author ledvima1
 */
public class PersonIntruderDto extends RunwayIntruderDto {

    private String category;

    // TODO This can use the organization directly
    private String organization;

    private String wasDoing;

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
