package cz.cvut.kbss.inbas.audit.rest.dto.model.incursion;

import cz.cvut.kbss.inbas.audit.model.AircraftEvent;
import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessmentDto;

/**
 * @author ledvima1
 */
public class RunwayIncursionDto extends EventTypeAssessmentDto {

    private LowVisibilityProcedure lvp;

    private Location location;

    private AircraftEvent conflictingAircraft;

    private RunwayIntruderDto intruder;

    public LowVisibilityProcedure getLvp() {
        return lvp;
    }

    public void setLvp(LowVisibilityProcedure lvp) {
        this.lvp = lvp;
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

    public RunwayIntruderDto getIntruder() {
        return intruder;
    }

    public void setIntruder(RunwayIntruderDto intruder) {
        this.intruder = intruder;
    }

    @Override
    public String toString() {
        return "RunwayIncursionDto{" +
                "eventType=" + getEventType() +
                ", intruder=" + intruder +
                ", conflictingAircraft=" + conflictingAircraft +
                "}";
    }
}
