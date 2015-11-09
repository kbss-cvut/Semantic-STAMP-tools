package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class PreliminaryReportDao extends BaseDao<PreliminaryReport> {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private EventTypeDao eventTypeDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private InitialReportDao initialReportDao;

    public PreliminaryReportDao() {
        super(PreliminaryReport.class);
    }

    @Override
    protected void persist(PreliminaryReport entity, EntityManager em) {
        if (entity.getRevision() == 1) {
            occurrenceDao.persist(entity.getOccurrence(), em);
        }
        saveEventTypes(entity.getTypeAssessments(), em);
        saveInitialReports(entity.getInitialReports(), em);
        entity.generateKey();
        em.persist(entity);
    }

    @Override
    public void persist(PreliminaryReport entity) {
        Objects.requireNonNull(entity);

        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            persist(entity, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public void persist(Collection<PreliminaryReport> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) {
            return;
        }

        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            entities.forEach(e -> persist(e, em));
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    private void saveEventTypes(Set<EventTypeAssessment> typeAssessments, EntityManager em) {
        if (typeAssessments == null || typeAssessments.isEmpty()) {
            return;
        }
        for (EventTypeAssessment assessment : typeAssessments) {
            if (!eventTypeDao.exists(assessment.getEventType().getId(), em)) {
                em.persist(assessment.getEventType());
            }
            if (assessment.getRunwayIncursion() != null) {
                saveIncursionLocation(assessment.getRunwayIncursion(), em);
                saveIncursionOrganizations(assessment.getRunwayIncursion(), em);
            }
        }
    }

    private void saveIncursionLocation(RunwayIncursion incursion, EntityManager em) {
        if (incursion.getLocation() != null) {
            final Location location = incursion.getLocation();
            if (!locationDao.exists(location.getUri(), em)) {
                locationDao.persist(location, em);
            }
        }
    }

    private void saveIncursionOrganizations(RunwayIncursion incursion, EntityManager em) {
        if (incursion.getConflictingAircraft() != null) {
            saveOrganizationIfNotExists(incursion.getConflictingAircraft().getOperator(), em);
        }
        if (incursion.getIntruder() != null) {
            if (incursion.getIntruder().getAircraft() != null) {
                saveOrganizationIfNotExists(incursion.getIntruder().getAircraft().getOperator(), em);
            } else if (incursion.getIntruder().getVehicle() != null) {
                saveOrganizationIfNotExists(incursion.getIntruder().getVehicle().getOrganization(), em);
            } else if (incursion.getIntruder().getPerson() != null) {
                saveOrganizationIfNotExists(incursion.getIntruder().getPerson().getOrganization(), em);
            }
        }
    }

    private void saveOrganizationIfNotExists(Organization organization, EntityManager em) {
        if (organization == null) {
            return;
        }
        organization.generateUri();
        if (!organizationDao.exists(organization.getUri(), em)) {
            em.persist(organization);
        }
    }

    private void saveInitialReports(Set<InitialReport> initialReports, EntityManager em) {
        if (initialReports == null) {
            return;
        }
        initialReports.stream().filter(ir -> !initialReportDao.exists(ir.getUri(), em)).forEach(ir -> {
            ir.generateKey();
            initialReportDao.persist(ir, em);
        });
    }

    @Override
    public void update(PreliminaryReport entity) {
        Objects.requireNonNull(entity);

        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            saveEventTypes(entity.getTypeAssessments(), em);
            saveInitialReports(entity.getInitialReports(), em);
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    /**
     * Gets all preliminary reports for the specified occurrence.
     *
     * @param occurrence Occurrence to filter reports by
     * @return List of matching reports
     */
    public List<PreliminaryReport> findByOccurrence(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);

        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?r WHERE { ?r <" + Vocabulary.p_hasOccurrence + "> ?occurrence ;" +
                            "rdf:type <" + Vocabulary.PreliminaryReport + "> . }",
                    PreliminaryReport.class).setParameter("occurrence", occurrence.getUri()).getResultList();
        } finally {
            em.close();
        }
    }
}
