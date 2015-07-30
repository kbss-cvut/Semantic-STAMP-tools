package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.*;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

/**
 * @author ledvima1
 */
@Repository
public class ReportDao extends BaseDao<OccurrenceReport> {

    @Autowired
    private EventTypeDao eventTypeDao;

    @Autowired
    private OrganizationDao organizationDao;

    public ReportDao() {
        super(OccurrenceReport.class);
    }

    @Override
    public void persist(OccurrenceReport entity) {
        Objects.requireNonNull(entity);

        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            saveEventTypes(entity.getTypeAssessments(), em);
            entity.generateKey();
            em.persist(entity);
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
                saveIncursionOrganizations(assessment.getRunwayIncursion(), em);
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

    @Override
    public void update(OccurrenceReport entity) {
        Objects.requireNonNull(entity);

        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            saveEventTypes(entity.getTypeAssessments(), em);
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }
}
