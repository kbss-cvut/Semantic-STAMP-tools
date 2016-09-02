package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import cz.cvut.kbss.inbas.reporting.persistence.dao.util.OrphanRemover;
import cz.cvut.kbss.inbas.reporting.persistence.dao.util.QuestionSaver;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Repository
public class AuditDao extends BaseDao<Audit> {

    @Autowired
    private OrganizationDao organizationDao;

    public AuditDao() {
        super(Audit.class);
    }

    @Override
    protected void persist(Audit entity, EntityManager em) {
        if (entity.getAuditee().getUri() == null || !organizationDao.exists(entity.getAuditee().getUri(), em)) {
            organizationDao.persist(entity.getAuditee(), em);
        }
        if (entity.getQuestion() != null) {
            final QuestionSaver questionSaver = new QuestionSaver();
            questionSaver.persistIfNecessary(entity.getQuestion(), em);
        }
        if (entity.getFindings() != null) {
            persistCorrectiveMeasures(entity, em);
        }
        super.persist(entity, em);
    }

    private void persistCorrectiveMeasures(Audit audit, EntityManager em) {
        traverseCorrectiveMeasures(audit, cm -> cm.getUri() == null, em::persist);
    }

    private void traverseCorrectiveMeasures(Audit audit, Predicate<CorrectiveMeasureRequest> filter,
                                            Consumer<CorrectiveMeasureRequest> consumer) {
        if (audit.getFindings() != null) {
            if (filter != null) {
                audit.getFindings().stream().filter(f -> f.getCorrectiveMeasures() != null)
                     .forEach(f -> f.getCorrectiveMeasures().stream().filter(filter).forEach(consumer));
            } else {
                audit.getFindings().stream().filter(f -> f.getCorrectiveMeasures() != null)
                     .forEach(f -> f.getCorrectiveMeasures().forEach(consumer));
            }
        }
    }

    @Override
    protected void update(Audit entity, EntityManager em) {
        final Audit original = em.find(Audit.class, entity.getUri());
        final Map<URI, MeasureCounter> measureMap = mapCorrectiveMeasures(original);
        final OrphanRemover remover = new OrphanRemover(em);
        remover.removeOrphans(original.getFindings(), entity.getFindings());
        reduceActualMeasures(entity, measureMap);
        removeOrphansAndUpdateOthers(measureMap, em);
        updateExistingCorrectiveMeasures(entity, em);
        persistCorrectiveMeasures(entity, em);
        super.update(entity, em);
    }

    private Map<URI, MeasureCounter> mapCorrectiveMeasures(Audit entity) {
        final Map<URI, MeasureCounter> map = new HashMap<>();
        traverseCorrectiveMeasures(entity, null, m -> map.put(m.getUri(), new MeasureCounter(m)));
        return map;
    }

    private void reduceActualMeasures(Audit entity, Map<URI, MeasureCounter> map) {
        traverseCorrectiveMeasures(entity, m -> map.containsKey(m.getUri()), m -> map.get(m.getUri()).counter++);
    }

    private void removeOrphansAndUpdateOthers(Map<URI, MeasureCounter> map, EntityManager em) {
        map.values().stream().filter(counter -> counter.counter == 0).forEach(counter -> em.remove(counter.measure));
    }

    private void updateExistingCorrectiveMeasures(Audit entity, EntityManager em) {
        traverseCorrectiveMeasures(entity, cm -> cm.getUri() != null, em::merge);
    }

    @Override
    protected void remove(Audit entity, EntityManager em) {
        final Set<URI> removedMeasures = new HashSet<>();
        traverseCorrectiveMeasures(entity, m -> !removedMeasures.contains(m.getUri()), m -> {
            removedMeasures.add(m.getUri());
            final CorrectiveMeasureRequest toRemove = em.merge(m);
            em.remove(toRemove);
        });
        super.remove(entity, em);
    }

    private static final class MeasureCounter {
        private final CorrectiveMeasureRequest measure;
        private int counter = 0;

        private MeasureCounter(CorrectiveMeasureRequest measure) {
            this.measure = measure;
        }
    }
}
