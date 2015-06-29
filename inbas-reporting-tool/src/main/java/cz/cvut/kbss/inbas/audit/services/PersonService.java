package cz.cvut.kbss.inbas.audit.services;

import cz.cvut.kbss.inbas.audit.model.Person;

/**
 * @author ledvima1
 */
public interface PersonService extends InbasService<Person> {

    /**
     * Finds person by his/her username.
     *
     * @param username Person's username
     * @return Person or {@code null} in none matches the username
     */
    Person findByUsername(String username);
}
