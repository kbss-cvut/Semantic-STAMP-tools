package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.model.reports.EventType;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventTypeAssessmentDao extends BaseDao<EventTypeAssessment> {

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private AircraftDao aircraftDao;

    @Autowired
    private OrganizationDao organizationDao;

    public EventTypeAssessmentDao() {
        super(EventTypeAssessment.class);
    }

    @Override
    protected void persist(EventTypeAssessment entity, EntityManager em) {
        persistEventTypeIfNecessary(entity, em);
        if (entity.getRunwayIncursion() != null) {
            saveIncursionLocation(entity.getRunwayIncursion(), em);
            saveIncursionOrganizations(entity.getRunwayIncursion(), em);
        }
        em.persist(entity);
    }

    private void persistEventTypeIfNecessary(EventTypeAssessment entity, EntityManager em) {
        // Have to ask the em directly, because dao.exists uses an ASK query, which does not currently support changes in transaction
        if (entity.getEventType() != null && em.find(EventType.class, entity.getEventType().getId()) == null) {
            em.persist(entity.getEventType());
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
            saveAircraftIfNotExists(incursion.getConflictingAircraft().getAircraft(), em);
        }
        if (incursion.getIntruder() != null) {
            if (incursion.getIntruder().getAircraft() != null) {
                saveAircraftIfNotExists(incursion.getIntruder().getAircraft().getAircraft(), em);
            } else if (incursion.getIntruder().getVehicle() != null) {
                saveOrganizationIfNotExists(incursion.getIntruder().getVehicle().getOrganization(), em);
            } else if (incursion.getIntruder().getPerson() != null) {
                saveOrganizationIfNotExists(incursion.getIntruder().getPerson().getOrganization(), em);
            }
        }
    }

    private void saveAircraftIfNotExists(Aircraft aircraft, EntityManager em) {
        if (aircraft == null) {
            return;
        }
        if (!aircraftDao.exists(aircraft.getUri(), em)) {
            aircraftDao.persist(aircraft, em);
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
        persistEventTypeIfNecessary(entity, em);
        if (entity.getRunwayIncursion() != null) {
            saveIncursionLocation(entity.getRunwayIncursion(), em);
            saveIncursionOrganizations(entity.getRunwayIncursion(), em);
        }
        em.merge(entity);
    }
}
