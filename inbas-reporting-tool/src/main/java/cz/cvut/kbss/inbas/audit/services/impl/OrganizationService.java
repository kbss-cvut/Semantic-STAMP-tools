package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OrganizationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ledvima1
 */
@Service
public class OrganizationService extends BaseService<Organization> {

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    protected GenericDao<Organization> getPrimaryDao() {
        return organizationDao;
    }

    public Organization findByName(String name) {
        return organizationDao.findByName(name);
    }
}
