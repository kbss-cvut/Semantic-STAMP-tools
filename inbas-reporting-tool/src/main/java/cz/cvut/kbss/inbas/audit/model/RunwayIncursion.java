package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.RunwayIncursion)
public class RunwayIncursion {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_lowVisibilityProcedure)
    private LowVisibilityProcedure lowVisibilityProcedure;

    @OWLObjectProperty(iri = Vocabulary.p_hasClearedAircraft, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Aircraft conflictingAircraft;

    @OWLObjectProperty(iri = Vocabulary.p_hasIntruder, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Intruder intruder;

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

    public Aircraft getConflictingAircraft() {
        return conflictingAircraft;
    }

    public void setConflictingAircraft(Aircraft conflictingAircraft) {
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
