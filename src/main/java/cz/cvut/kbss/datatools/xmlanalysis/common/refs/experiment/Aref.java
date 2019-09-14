package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;

public class Aref {
    @FIDAttribute(cls = A.class, value = "1")
    protected String name;
    @FIDAttribute(fieldRef = "secondName", value = "1")
    protected String lastName;

    public Aref(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Aref{" +
                "\n\tname='" + name + '\'' + "," +
                "\n\tlastName='" + lastName + '\'' + "\n" +
                '}';
    }
}
