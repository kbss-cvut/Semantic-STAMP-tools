package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.Organization;

public interface OrganizationService extends BaseService<Organization> {

    Organization findByName(String name);
}
