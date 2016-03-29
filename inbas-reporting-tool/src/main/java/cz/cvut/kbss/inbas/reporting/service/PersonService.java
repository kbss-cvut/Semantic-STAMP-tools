package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.model_new.Person;

public interface PersonService extends BaseService<Person> {

    Person findByUsername(String username);
}
