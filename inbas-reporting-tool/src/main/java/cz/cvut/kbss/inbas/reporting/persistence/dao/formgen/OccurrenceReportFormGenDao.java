package cz.cvut.kbss.inbas.reporting.persistence.dao.formgen;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.util.QuestionSaver;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import org.springframework.stereotype.Repository;

import java.util.IdentityHashMap;
import java.util.Map;

@Repository
public class OccurrenceReportFormGenDao extends FormGenDao<OccurrenceReport> {

    @Override
    void prePersist(OccurrenceReport instance, EntityManager em, Descriptor descriptor) {
        // We need to set some default values (if necessary), so that the instance passes IC validation
        if (instance.getKey() == null || instance.getKey().isEmpty()) {
            instance.setKey(IdentificationUtils.generateKey());
        }
        if (instance.getRevision() == null) {
            instance.setRevision(0);
        }
        if (instance.getFileNumber() == null) {
            instance.setFileNumber(System.currentTimeMillis());
        }
        if (instance.getOccurrence().getKey() == null || instance.getOccurrence().getKey().isEmpty()) {
            instance.getOccurrence().setKey(IdentificationUtils.generateKey());
        }
        em.persist(instance.getAuthor(), descriptor);
        if (instance.getLastModifiedBy() != null &&
                !instance.getLastModifiedBy().getUri().equals(instance.getAuthor().getUri())) {
            em.persist(instance.getLastModifiedBy(), descriptor);
        }
        persistEventsIfNecessary(instance.getOccurrence(), em, descriptor);
        em.persist(instance.getOccurrence(), descriptor);
        if (instance.getCorrectiveMeasures() != null) {
            instance.getCorrectiveMeasures().clear();
        }
        super.prePersist(instance, em, descriptor);
    }

    private void persistEventsIfNecessary(Occurrence entity, EntityManager em, Descriptor descriptor) {
        final Map<Event, Object> visited = new IdentityHashMap<>();
        final QuestionSaver questionSaver = new QuestionSaver(descriptor);
        if (entity.getChildren() != null) {
            entity.getChildren().forEach(e -> persistEventIfNecessary(e, em, descriptor, visited, questionSaver));
        }
        if (entity.getFactors() != null) {
            entity.getFactors()
                  .forEach(f -> persistEventIfNecessary(f.getEvent(), em, descriptor, visited, questionSaver));
        }
    }

    private void persistEventIfNecessary(Event event, final EntityManager em, Descriptor descriptor,
                                         Map<Event, Object> visited, QuestionSaver questionSaver) {
        if (visited.containsKey(event)) {
            return;
        }
        visited.put(event, null);
        if (event.getChildren() != null) {
            event.getChildren().forEach(e -> persistEventIfNecessary(e, em, descriptor, visited, questionSaver));
        }
        if (event.getFactors() != null) {
            event.getFactors()
                 .forEach(f -> persistEventIfNecessary(f.getEvent(), em, descriptor, visited, questionSaver));
        }
        em.persist(event, descriptor);
        if (event.getQuestion() != null) {
            questionSaver.persistIfNecessary(event.getQuestion(), em);
        }
    }
}
