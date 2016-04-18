package cz.cvut.kbss.inbas.reporting.model.arms;


import cz.cvut.kbss.inbas.reporting.dto.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Most probable outcome of an event, in case it escalated into an accident.
 * <p>
 * As per ARMS, Event Risk Classification (ERC).
 */
public enum AccidentOutcome {

    /**
     * No potential damage or injury could occur.
     */
    NEGLIGIBLE("No potential damage or injury could occur"),
    /**
     * Minor injuries, minor damage to the aircraft.
     */
    MINOR("Minor injuries, minor damage to the aircraft"),
    /**
     * 1 or 2 fatalities, multiple serious injuries, major damage to the aircraft.
     */
    MAJOR("1 or 2 fatalities, multiple serious injuries, major damage to the aircraft"),
    /**
     * Loss of aircraft or multiple fatalities (3 or more).
     */
    CATASTROPHIC("Loss of aircraft or multiple fatalities (3 or more)");

    private final String description;

    AccidentOutcome(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<Pair<AccidentOutcome, String>> toPairs() {
        final List<Pair<AccidentOutcome, String>> lst = new ArrayList<>();
        for (AccidentOutcome val : values()) {
            lst.add(new Pair<>(val, val.getDescription()));
        }
        return lst;
    }
}
