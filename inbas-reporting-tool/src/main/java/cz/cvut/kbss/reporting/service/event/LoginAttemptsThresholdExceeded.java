package cz.cvut.kbss.reporting.service.event;

import cz.cvut.kbss.reporting.model.Person;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

/**
 * Event emitted when a user exceeds the maximum number ({@link cz.cvut.kbss.reporting.security.SecurityConstants#MAX_LOGIN_ATTEMPTS})
 * of unsuccessful login attempts.
 */
public class LoginAttemptsThresholdExceeded extends ApplicationEvent {

    private final Person user;

    public LoginAttemptsThresholdExceeded(Object source, Person user) {
        super(source);
        this.user = Objects.requireNonNull(user);
    }

    /**
     * The user who exceeded unsuccessful login attempts maximum.
     *
     * @return Person instance
     */
    public Person getUser() {
        return user;
    }
}
