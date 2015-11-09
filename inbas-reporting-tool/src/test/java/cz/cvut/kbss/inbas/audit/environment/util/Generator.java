package cz.cvut.kbss.inbas.audit.environment.util;

import cz.cvut.kbss.inbas.audit.model.Occurrence;

import java.util.Date;
import java.util.UUID;

public class Generator {

    private Generator() {
        throw new AssertionError();
    }

    public static Occurrence generateOccurrence() {
        final Occurrence occurrence = new Occurrence();
        occurrence.setName(UUID.randomUUID().toString());
        occurrence.setStartTime(new Date(System.currentTimeMillis() - 10000));
        occurrence.setEndTime(new Date());
        return occurrence;
    }
}
