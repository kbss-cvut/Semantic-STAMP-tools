package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.we;

import cz.cvut.kbss.datatools.xmlanalysis.experiments.model.ADOXMLModelView;

import java.util.ArrayList;
import java.util.List;

public class WorkingEnvironmentModel extends ADOXMLModelView {
    protected List<Role> roles = new ArrayList<>();
    protected List<Performer> performers = new ArrayList<>();
    protected List<OrganizationalUnit> organizationalUnits = new ArrayList<>();
    protected List<HasRole> hasRoles = new ArrayList<>();
    protected List<BelongsTo> belongsTos = new ArrayList<>();

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Performer> performers) {
        this.performers = performers;
    }

    public List<OrganizationalUnit> getOrganizationalUnits() {
        return organizationalUnits;
    }

    public void setOrganizationalUnits(List<OrganizationalUnit> organizationalUnits) {
        this.organizationalUnits = organizationalUnits;
    }

    public List<HasRole> getHasRoles() {
        return hasRoles;
    }

    public void setHasRoles(List<HasRole> hasRoles) {
        this.hasRoles = hasRoles;
    }

    public List<BelongsTo> getBelongsTos() {
        return belongsTos;
    }

    public void setBelongsTos(List<BelongsTo> belongsTos) {
        this.belongsTos = belongsTos;
    }
}
