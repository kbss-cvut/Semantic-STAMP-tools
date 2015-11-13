package cz.cvut.kbss.inbas.audit.model.reports.incursions;

import cz.cvut.kbss.inbas.audit.model.AircraftEvent;
import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.RunwayIncursion)
public class RunwayIncursion implements Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_lowVisibilityProcedure)
    private LowVisibilityProcedure lowVisibilityProcedure;

    @OWLObjectProperty(iri = Vocabulary.p_hasLocation, fetch = FetchType.EAGER)
    private Location location;

    @OWLObjectProperty(iri = Vocabulary.p_hasClearedAircraft, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AircraftEvent conflictingAircraft;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasIntruder, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Intruder intruder;

    public RunwayIncursion() {
    }

    public RunwayIncursion(RunwayIncursion other) {
        this.location = other.location;
        this.lowVisibilityProcedure = other.lowVisibilityProcedure;
        if (other.conflictingAircraft != null) {
            this.conflictingAircraft = new AircraftEvent(other.conflictingAircraft);
        }
        assert other.intruder != null;
        this.intruder = new Intruder(other.intruder);
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public LowVisibilityProcedure getLowVisibilityProcedure() {
        return lowVisibilityProcedure;
    }

    public void setLowVisibilityProcedure(LowVisibilityProcedure lowVisibilityProcedure) {
        this.lowVisibilityProcedure = lowVisibilityProcedure;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AircraftEvent getConflictingAircraft() {
        return conflictingAircraft;
    }

    public void setConflictingAircraft(AircraftEvent conflictingAircraft) {
        this.conflictingAircraft = conflictingAircraft;
    }

    public Intruder getIntruder() {
        return intruder;
    }

    public void setIntruder(Intruder intruder) {
        this.intruder = intruder;
    }

    @Override
    public String toString() {
        return "RunwayIncursion{" +
                "conflictingAircraft=" + conflictingAircraft +
                ", intruder=" + intruder +
                '}';
    }
}
