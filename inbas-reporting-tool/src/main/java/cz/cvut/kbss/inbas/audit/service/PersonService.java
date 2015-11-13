package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.Person;

public interface PersonService extends BaseService<Person> {

    Person findByUsername(String username);
}
