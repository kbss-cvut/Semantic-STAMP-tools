package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.we;

public class BelongsTo {

    protected OrganizationalUnit organizationalUnit;
    protected Performer performer;

    public OrganizationalUnit getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(OrganizationalUnit organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }
}
