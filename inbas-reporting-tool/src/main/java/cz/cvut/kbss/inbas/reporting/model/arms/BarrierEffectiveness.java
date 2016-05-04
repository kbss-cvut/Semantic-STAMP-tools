package cz.cvut.kbss.inbas.reporting.model.arms;

/**
 * Specifies effectiveness of barriers between an event and the most probable accident scenario.
 * <p>
 * As per ARMS, Event Risk Classification (ERC).
 */
public enum BarrierEffectiveness {

    EFFECTIVE, LIMITED, MINIMAL, NOT_EFFECTIVE;

    public static BarrierEffectiveness fromString(String str) {
        for (BarrierEffectiveness be : values()) {
            if (be.name().equalsIgnoreCase(str)) {
                return be;
            }
        }
        throw new IllegalArgumentException("Unknown " + BarrierEffectiveness.class.getSimpleName() + " value " + str);
    }
}
