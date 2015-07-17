package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.*;
import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

/**
 * @author ledvima1
 */
@Repository
public class ReportDao extends BaseDao<EventReport> {

    @Override
    public void persist(EventReport entity) {
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
            if (em.find(EventType.class, assessment.getEventType().getId()) == null) {
                em.persist(assessment.getEventType());
            }
            if (assessment.getRunwayIncursion() != null) {
                saveIncursionOrganizations(assessment.getRunwayIncursion(), em);
            }
        }
    }

    private void saveIncursionOrganizations(RunwayIncursion incursion, EntityManager em) {
        if (incursion.getClearedAircraft() != null) {
            saveOrganizationIfNotExists(incursion.getClearedAircraft().getOperator(), em);
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
        if (em.find(Organization.class, organization.getUri()) == null) {
            em.persist(organization);
        }
    }
}
