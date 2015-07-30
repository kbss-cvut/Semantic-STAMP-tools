package cz.cvut.kbss.inbas.audit.model.reports.incursions;

import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Vehicle)
public class Vehicle {

    public static final String INTRUDER_TYPE = "vehicle";

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_vehicleType)
    private String vehicleType;

    @OWLDataProperty(iri = Vocabulary.p_callSign)
    private String callSign;

    @OWLDataProperty(iri = Vocabulary.p_controlledByAts)
    private String isAtsUnit;

    @OWLDataProperty(iri = Vocabulary.p_radio)
    private String hasRadio;

    @OWLDataProperty(iri = Vocabulary.p_activityDescription)
    private String whatWasDoing;

    @OWLObjectProperty(iri = Vocabulary.p_memberOf)
    private Organization organization;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getIsAtsUnit() {
        return isAtsUnit;
    }

    public void setIsAtsUnit(String isAtsUnit) {
        this.isAtsUnit = isAtsUnit;
    }

    public String getHasRadio() {
        return hasRadio;
    }

    public void setHasRadio(String hasRadio) {
        this.hasRadio = hasRadio;
    }

    public String getWhatWasDoing() {
        return whatWasDoing;
    }

    public void setWhatWasDoing(String whatWasDoing) {
        this.whatWasDoing = whatWasDoing;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
