package cz.cvut.kbss.reporting.service.security;

import cz.cvut.kbss.reporting.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tracks login attempts.
 */
public interface LoginTracker {

    Logger LOG = LoggerFactory.getLogger(LoginTracker.class);

    /**
     * Registers an unsuccessful login attempt by the specified user.
     * <p>
     * This basically means that the user entered an incorrect password.
     *
     * @param user Use attempting to login
     */
    void unsuccessfulLoginAttempt(Person user);

    /**
     * Registers a successful login attempt by the specified user.
     * <p>
     * This basically means that the user entered the correct password and will be logged in.
     *
     * @param user User attempting to login
     */
    void successfulLoginAttempt(Person user);
}
