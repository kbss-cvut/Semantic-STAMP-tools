package cz.cvut.kbss.inbas.audit.environment.util;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;

import java.util.Date;
import java.util.UUID;

public class Generator {

    public static final String USERNAME = "halsey@unsc.org";

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

    public static Person generatePerson() {
        final Person person = new Person();
        person.setFirstName("Catherine");
        person.setLastName("Halsey");
        person.setUsername(USERNAME);
        person.setPassword("john117");
        person.generateUri();
        return person;
    }
}
