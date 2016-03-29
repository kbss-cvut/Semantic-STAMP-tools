package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.model_new.Organization;

public interface OrganizationService extends BaseService<Organization> {

    Organization findByName(String name);
}
