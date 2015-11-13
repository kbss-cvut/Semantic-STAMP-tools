package cz.cvut.kbss.inbas.audit.service.repository;

import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OrganizationDao;
import cz.cvut.kbss.inbas.audit.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryOrganizationService extends BaseRepositoryService<Organization> implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    protected GenericDao<Organization> getPrimaryDao() {
        return organizationDao;
    }

    @Override
    public Organization findByName(String name) {
        return organizationDao.findByName(name);
    }
}
