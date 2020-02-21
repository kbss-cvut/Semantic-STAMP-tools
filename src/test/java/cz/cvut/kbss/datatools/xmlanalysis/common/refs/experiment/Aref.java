package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;

public class Aref {
    @FIDAttribute(cls = A.class)
    public String name;
    @FIDAttribute(fieldRef = "secondName")
    public String lastName;

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
