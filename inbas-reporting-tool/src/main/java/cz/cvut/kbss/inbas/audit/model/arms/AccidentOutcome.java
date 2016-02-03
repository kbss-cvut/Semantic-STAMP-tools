package cz.cvut.kbss.inbas.audit.model.arms;

/**
 * Most probable outcome of an event, in case it escalated into an accident.
 * <p>
 * As per ARMS, Event Risk Classification (ERC).
 */
public enum AccidentOutcome {

    /**
     * Loss of aircraft or multiple fatalities (3 or more).
     */
    CATASTROPHIC,
    /**
     * 1 or 2 fatalities, multiple serious injuries, major damage to the aircraft.
     */
    MAJOR,
    /**
     * Minor injuries, minor damage to the aircraft.
     */
    MINOR,
    /**
     * No potential damage or injury could occur.
     */
    NEGLIGIBLE
}
