package cz.cvut.kbss.reporting.service.security;

import cz.cvut.kbss.reporting.model.Person;

/**
 * Dummy login tracker which does nothing.
 */
public class DisabledLoginTracker implements LoginTracker {

    @Override
    public void unsuccessfulLoginAttempt(Person user) {
        // Do nothing
    }

    @Override
    public void successfulLoginAttempt(Person user) {
        // Do nothing
    }
}
