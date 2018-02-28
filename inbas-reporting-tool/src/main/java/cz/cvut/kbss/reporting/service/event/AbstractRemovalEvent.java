package cz.cvut.kbss.reporting.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * Parent class for removal events occurring in the application.
 * <p>
 * Such an event can be for example removal of a report.
 */
public abstract class AbstractRemovalEvent extends ApplicationEvent {

    public AbstractRemovalEvent(Object source) {
        super(source);
    }
}
