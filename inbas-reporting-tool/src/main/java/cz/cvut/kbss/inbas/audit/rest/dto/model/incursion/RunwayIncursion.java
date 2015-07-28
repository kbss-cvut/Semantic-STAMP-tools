package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment;

/**
 * @author ledvima1
 */
public class RunwayIncursion extends EventTypeAssessment {

    private LowVisibilityProcedure lvp;

    private Aircraft conflictingAircraft;

    private RunwayIntruder intruder;

    public RunwayIncursion() {
    }

    public RunwayIncursion(cz.cvut.kbss.inbas.audit.model.RunwayIncursion incursion) {
        super(incursion.getUri());
        lvp = incursion.getLowVisibilityProcedure();
        this.conflictingAircraft = incursion.getConflictingAircraft();
    }

    public LowVisibilityProcedure getLvp() {
        return lvp;
    }

    public void setLvp(LowVisibilityProcedure lvp) {
        this.lvp = lvp;
    }

    public Aircraft getConflictingAircraft() {
        return conflictingAircraft;
    }

    public void setConflictingAircraft(Aircraft conflictingAircraft) {
        this.conflictingAircraft = conflictingAircraft;
    }

    public RunwayIntruder getIntruder() {
        return intruder;
    }

    public void setIntruder(RunwayIntruder intruder) {
        this.intruder = intruder;
    }

    @Override
    public String toString() {
        return "RunwayIncursionDto{" +
                "eventType=" + getEventType() +
                ", lvp='" + lvp + '\'' +
                ", conflictingAircraft=" + conflictingAircraft +
                ", intruder=" + intruder +
                "}";
    }
}
