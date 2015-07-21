package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Organization;
import org.springframework.stereotype.Repository;

/**
 * @author ledvima1
 */
@Repository
public class OrganizationDao extends BaseDao<Organization> {

    public OrganizationDao() {
        super(Organization.class);
    }
}
