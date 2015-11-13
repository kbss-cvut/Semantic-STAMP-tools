package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AircraftDao extends BaseDao<Aircraft> {

    @Autowired
    private OrganizationDao organizationDao;

    public AircraftDao() {
        super(Aircraft.class);
    }

    @Override
    protected void persist(Aircraft entity, EntityManager em) {
        if (entity.getOperator() != null && !organizationDao.exists(entity.getOperator().getUri(), em)) {
            organizationDao.persist(entity.getOperator(), em);
        }
        super.persist(entity, em);
    }

    @Override
    protected void update(Aircraft entity, EntityManager em) {
        if (entity.getOperator() != null && !organizationDao.exists(entity.getOperator().getUri(), em)) {
            organizationDao.persist(entity.getOperator(), em);
        }
        super.update(entity, em);
    }
}
