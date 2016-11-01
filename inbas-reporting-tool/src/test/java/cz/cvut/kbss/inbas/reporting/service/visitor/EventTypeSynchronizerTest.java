package cz.cvut.kbss.inbas.reporting.service.visitor;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;

public class EventTypeSynchronizerTest extends BaseServiceTestRunner {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private EventTypeSynchronizer synchronizer;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void addsEventTypeForOccurrenceToItsTypesForNewOccurrence() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrence.getEventTypes().forEach(t -> occurrence.getTypes().remove(t.toString()));
        occurrence.getEventTypes().forEach(t -> assertFalse(occurrence.getTypes().contains(t.toString())));
        occurrence.accept(synchronizer);
        occurrence.getEventTypes().forEach(t -> assertTrue(occurrence.getTypes().contains(t.toString())));
    }

    @Test
    public void removesOriginalEventTypeWhenNewOneWasSetOnOccurrence() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final Set<URI> originalType = occurrence.getEventTypes();
        final Set<URI> newType = Collections.singleton(Generator.generateEventType());
        occurrence.setEventTypes(newType);
        occurrence.accept(synchronizer);
        assertEquals(newType, occurrence.getEventTypes());
        newType.forEach(t -> assertTrue(occurrence.getTypes().contains(t.toString())));
        originalType.forEach(t -> assertFalse(occurrence.getTypes().contains(t.toString())));
    }

    @Test
    public void leaveOtherTypesIntactWhenSynchronizingUpdatedTypesOnOccurrence() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final Set<URI> originalType = occurrence.getEventTypes();
        final Set<String> origTypes = occurrence.getTypes();
        originalType.forEach(t -> origTypes.remove(t.toString()));
        occurrence.setEventTypes(Collections.singleton(Generator.generateEventType()));
        occurrence.accept(synchronizer);
        originalType.forEach(t -> assertFalse(occurrence.getTypes().contains(t.toString())));
        assertTrue(occurrence.getTypes().containsAll(origTypes));
    }

    @Test
    public void addsEventTypeToTypesForNewEvent() {
        final Event event = new Event();
        event.setEventTypes(Collections.singleton(Generator.generateEventType()));
        event.getEventTypes().forEach(t -> event.getTypes().remove(t.toString()));
        event.getEventTypes().forEach(t -> assertFalse(event.getTypes().contains(t.toString())));
        event.accept(synchronizer);
        event.getEventTypes().forEach(t -> assertTrue(event.getTypes().contains(t.toString())));
    }

    @Test
    public void removesOriginalEventTypeWhenNewOneIsSetOnEvent() {
        final Event event = new Event();
        event.setEventTypes(Collections.singleton(Generator.generateEventType()));
        event.setStartTime(new Date());
        event.setEndTime(new Date());
        persistEvent(event);
        final Set<URI> originalType = event.getEventTypes();
        final Set<URI> newEventType = Collections.singleton(Generator.generateEventType());
        event.setEventTypes(newEventType);
        event.accept(synchronizer);
        assertEquals(newEventType, event.getEventTypes());
        originalType.forEach(t -> assertFalse(event.getTypes().contains(t.toString())));
        newEventType.forEach(t -> assertTrue(event.getTypes().contains(t.toString())));
    }

    private void persistEvent(Event event) {
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(event);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void onlyAddsEventTypeToTypesWhenOriginalEventTypeIsNull() {
        final Event event = new Event();
        event.setStartTime(new Date());
        event.setEndTime(new Date());
        persistEvent(event);
        final Set<URI> newEventType = Collections.singleton(Generator.generateEventType());
        event.setEventTypes(newEventType);
        event.accept(synchronizer);
        assertEquals(newEventType, event.getEventTypes());
        newEventType.forEach(t -> assertTrue(event.getTypes().contains(t.toString())));
    }

    @Test
    public void removesTypeFromTypesWhenSetToNull() {
        final Event event = new Event();
        event.setStartTime(new Date());
        event.setEndTime(new Date());
        event.setEventTypes(Collections.singleton(Generator.generateEventType()));
        persistEvent(event);
        final Set<URI> originalType = event.getEventTypes();
        event.setEventTypes(null);
        event.accept(synchronizer);
        originalType.forEach(t -> assertFalse(event.getTypes().contains(t.toString())));
    }

    @Test
    public void leaveOtherTypesIntactWhenSynchronizingUpdatedTypesOnEvent() {
        final Event event = new Event();
        event.setStartTime(new Date());
        event.setEndTime(new Date());
        event.setEventTypes(Collections.singleton(Generator.generateEventType()));
        persistEvent(event);
        final Set<URI> originalType = event.getEventTypes();
        final Set<String> origTypes = event.getTypes();
        originalType.forEach(t -> origTypes.remove(t.toString()));
        event.setEventTypes(Collections.singleton(Generator.generateEventType()));
        event.accept(synchronizer);
        originalType.forEach(t -> assertFalse(event.getTypes().contains(t.toString())));
        assertTrue(event.getTypes().containsAll(origTypes));
    }
}
