package cz.cvut.kbss.inbas.audit.beans;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ledvima1 on 16.4.15.
 */

@Component("personsBean")
@Scope("request")
public class PersonsBean {

    @Autowired
    private PersonService personService;

    public List<Person> getPersons() {
        return personService.findAll();
    }
}
