package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.we;

import java.util.List;

public class OrganizationalUnit {

    protected List<Performer> performers;

    protected List<BelongsTo> belongsTos;

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    public List<BelongsTo> getBelongsTos() {
        return belongsTos;
    }

    public void setBelongsTos(List<BelongsTo> belongsTos) {
        this.belongsTos = belongsTos;
    }
}
