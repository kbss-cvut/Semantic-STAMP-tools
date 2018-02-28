package cz.cvut.kbss.reporting.service.event;

import cz.cvut.kbss.reporting.model.AbstractReport;
import cz.cvut.kbss.reporting.model.Resource;

import java.util.Objects;

/**
 * Signifies that a resource was removed from its related report and thus from the ontology.
 */
public class ResourceRemovalEvent extends AbstractRemovalEvent {

    private final AbstractReport report;
    private final Resource resource;

    public ResourceRemovalEvent(Object source, AbstractReport report, Resource resource) {
        super(source);
        this.report = Objects.requireNonNull(report);
        this.resource = Objects.requireNonNull(resource);
    }

    /**
     * Gets the report from which the resource was removed.
     *
     * @return Resource owner
     */
    public AbstractReport getReport() {
        return report;
    }

    /**
     * Gets the removed resource.
     *
     * @return Removed resource
     */
    public Resource getResource() {
        return resource;
    }
}
