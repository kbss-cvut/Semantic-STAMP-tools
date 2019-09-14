package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationalModel extends Model{

    // Fields and mappings
    @XmlPath("INSTANCE[@class='Performer']")
    protected List<Instance> performers;

    @XmlPath("INSTANCE[@class='Organizational unit']")
    protected List<Instance> organizationalUnits;

    @XmlPath("INSTANCE[@class='Role']")
    protected List<Instance> roles;

    @XmlPath("INSTANCE[@class='Aggregation']")
    protected List<Instance> aggregations;


    @XmlPath("CONNECTOR[@class='Belongs to']")
    protected List<Connector> performerBelongsToConnectors;

    @XmlPath("CONNECTOR[@class='Is manager']")
    protected List<Connector> performerIsManagerConnectors;

    @XmlPath("CONNECTOR[@class='Has role']")
    protected List<Connector> performerHasRoleConnectors;

    @XmlPath("CONNECTOR[@class='Is inside']")
    protected List<Connector> roleIsInsideConnectors;

    @XmlPath("CONNECTOR[@class='Is subordinated']")
    protected List<Connector> organizationalUnitIsSubordinateConnectors;


    // Getters and Setters
    public List<Instance> getPerformers() {
        return performers;
    }

    public void setPerformers(List<Instance> performers) {
        this.performers = performers;
    }

    public List<Instance> getOrganizationalUnits() {
        return organizationalUnits;
    }

    public void setOrganizationalUnits(List<Instance> organizationalUnits) {
        this.organizationalUnits = organizationalUnits;
    }

    public List<Instance> getRoles() {
        return roles;
    }

    public void setRoles(List<Instance> roles) {
        this.roles = roles;
    }

    public List<Instance> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<Instance> aggregations) {
        this.aggregations = aggregations;
    }

    public List<Connector> getPerformerBelongsToConnectors() {
        return performerBelongsToConnectors;
    }

    public void setPerformerBelongsToConnectors(List<Connector> performerBelongsToConnectors) {
        this.performerBelongsToConnectors = performerBelongsToConnectors;
    }

    public List<Connector> getPerformerIsManagerConnectors() {
        return performerIsManagerConnectors;
    }

    public void setPerformerIsManagerConnectors(List<Connector> performerIsManagerConnectors) {
        this.performerIsManagerConnectors = performerIsManagerConnectors;
    }

    public List<Connector> getPerformerHasRoleConnectors() {
        return performerHasRoleConnectors;
    }

    public void setPerformerHasRoleConnectors(List<Connector> performerHasRoleConnectors) {
        this.performerHasRoleConnectors = performerHasRoleConnectors;
    }

    public List<Connector> getRoleIsInsideConnectors() {
        return roleIsInsideConnectors;
    }

    public void setRoleIsInsideConnectors(List<Connector> roleIsInsideConnectors) {
        this.roleIsInsideConnectors = roleIsInsideConnectors;
    }

    public List<Connector> getOrganizationalUnitIsSubordinateConnectors() {
        return organizationalUnitIsSubordinateConnectors;
    }

    public void setOrganizationalUnitIsSubordinateConnectors(List<Connector> organizationalUnitIsSubordinateConnectors) {
        this.organizationalUnitIsSubordinateConnectors = organizationalUnitIsSubordinateConnectors;
    }
}
