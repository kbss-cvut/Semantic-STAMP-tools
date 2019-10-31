package cz.cvut.kbss.reporting.dto;

import cz.cvut.kbss.reporting.model.Person;

public class PersonUpdateDto extends Person {

    private String passwordOriginal;

    public String getPasswordOriginal() {
        return passwordOriginal;
    }

    public void setPasswordOriginal(String passwordOriginal) {
        this.passwordOriginal = passwordOriginal;
    }
}
