package cz.cvut.kbss.reporting.service.security;

import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import cz.cvut.kbss.reporting.service.event.LoginAttemptsThresholdExceeded;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks unsuccessful login attempts, but does not store the information in any persistent storage.
 * <p>
 * This means that when the application is shut down, the data about login attempts are lost.
 * <p>
 * When a threshold ({@link cz.cvut.kbss.reporting.security.SecurityConstants#MAX_LOGIN_ATTEMPTS}) is reached, an event
 * is published for the application. A successful login resets the counter of unsuccessful attempts for the user.
 */
public class RuntimeBasedLoginTracker implements LoginTracker, ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    private final Map<URI, AtomicInteger> counter = new ConcurrentHashMap<>();

    @Override
    public void unsuccessfulLoginAttempt(Person user) {
        Objects.requireNonNull(user);
        if (!counter.containsKey(user.getUri())) {
            counter.putIfAbsent(user.getUri(), new AtomicInteger());
        }
        final AtomicInteger cnt = counter.get(user.getUri());
        final int attempts = cnt.incrementAndGet();
        if (attempts > SecurityConstants.MAX_LOGIN_ATTEMPTS) {
            emitThresholdExceeded(user);
        }
    }

    private void emitThresholdExceeded(Person user) {
        if (counter.get(user.getUri()).get() > SecurityConstants.MAX_LOGIN_ATTEMPTS + 1) {
            // Do not emit multiple times
            return;
        }
        eventPublisher.publishEvent(new LoginAttemptsThresholdExceeded(this, user));
    }

    @Override
    public void successfulLoginAttempt(Person user) {
        Objects.requireNonNull(user);
        counter.computeIfPresent(user.getUri(), (uri, attempts) -> {
            attempts.set(0);
            return attempts;
        });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
