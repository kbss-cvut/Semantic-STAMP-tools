package cz.cvut.kbss.inbas.reporting.service.visitor;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EventDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Keeps event type and occurrence/event types in sync by adding or removing the event type to/from types.
 * <p>
 * For example when an occurrence is updated and its event type changes, the synchronizer removes the original event
 * type from types and adds the new one.
 */
@Service
public class EventTypeSynchronizer implements FactorGraphNodeVisitor {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private EventDao eventDao;

    @Override
    public void visit(Occurrence occurrence) {
        occurrence.setEventTypes(occurrence.getEventTypes());
        if (occurrence.getUri() != null) {
            final Occurrence original = occurrenceDao.find(occurrence.getUri());
            assert original != null;
            original.getEventTypes().forEach(t -> {
                if (!occurrence.getEventTypes().contains(t)) {
                    occurrence.getTypes().remove(t.toString());
                }
            });
        }
    }

    @Override
    public void visit(SafetyIssue issue) {
        // Do nothing
    }

    @Override
    public void visit(Event event) {
        event.setEventTypes(event.getEventTypes());
        if (event.getUri() != null) {
            final Event original = eventDao.find(event.getUri());
            assert original != null;
            if (original.getEventTypes() != null) {
                original.getEventTypes().forEach(t -> {
                    if (event.getEventTypes() == null || !event.getEventTypes().contains(t)) {
                        event.getTypes().remove(t.toString());
                    }
                });
            }
        }
    }
}
