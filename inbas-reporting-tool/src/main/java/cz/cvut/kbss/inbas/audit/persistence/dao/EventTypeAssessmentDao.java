package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventTypeAssessmentDao extends BaseDao<EventTypeAssessment> {

    @Autowired
    private EventTypeDao eventTypeDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private OrganizationDao organizationDao;

    public EventTypeAssessmentDao() {
        super(EventTypeAssessment.class);
    }

    @Override
    protected void persist(EventTypeAssessment entity, EntityManager em) {
        if (entity.getEventType() != null && !eventTypeDao.exists(entity.getEventType().getId(), em)) {
            em.persist(entity.getEventType());
        }
        if (entity.getRunwayIncursion() != null) {
            saveIncursionLocation(entity.getRunwayIncursion(), em);
            saveIncursionOrganizations(entity.getRunwayIncursion(), em);
        }
        em.persist(entity);
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

    @Override
    protected void update(EventTypeAssessment entity, EntityManager em) {
        if (!eventTypeDao.exists(entity.getEventType().getId(), em)) {
            em.persist(entity.getEventType());
        }
        if (entity.getRunwayIncursion() != null) {
            saveIncursionLocation(entity.getRunwayIncursion(), em);
            saveIncursionOrganizations(entity.getRunwayIncursion(), em);
        }
        em.merge(entity);
    }
}
