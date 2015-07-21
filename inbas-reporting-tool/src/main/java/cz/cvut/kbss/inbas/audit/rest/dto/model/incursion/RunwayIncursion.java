package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment;

/**
 * @author ledvima1
 */
public class RunwayIncursion extends EventTypeAssessment {

    private String lvp;

    private Aircraft clearedAircraft;

    private RunwayIntruder intruder;

    public RunwayIncursion() {
    }

    public RunwayIncursion(cz.cvut.kbss.inbas.audit.model.RunwayIncursion incursion) {
        super(incursion.getUri());
        lvp = incursion.getLowVisibilityProcedure();
        this.clearedAircraft = incursion.getClearedAircraft();
    }

    public String getLvp() {
        return lvp;
    }

    public void setLvp(String lvp) {
        this.lvp = lvp;
    }

    public Aircraft getClearedAircraft() {
        return clearedAircraft;
    }

    public void setClearedAircraft(Aircraft clearedAircraft) {
        this.clearedAircraft = clearedAircraft;
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
                ", clearedAircraft=" + clearedAircraft +
                ", intruder=" + intruder +
                "}";
    }
}
