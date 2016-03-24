package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.Location;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDao extends BaseDao<Location> {

    public LocationDao() {
        super(Location.class);
    }

    @Override
    protected void persist(Location entity, EntityManager em) {
        if (entity.getUri() == null) {
            entity.generateUri();
        }
        em.persist(entity);
    }
}
