package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class LocationDao extends BaseDao<Location> {

    public LocationDao() {
        super(Location.class);
    }

    @Override
    public void persist(Location entity) {
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
    protected void persist(Location entity, EntityManager em) {
        if (entity.getUri() == null) {
            entity.generateUri();
        }
        em.persist(entity);
    }
}
