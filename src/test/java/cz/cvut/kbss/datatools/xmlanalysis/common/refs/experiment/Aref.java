package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aref aref = (Aref) o;
        return Objects.equals(name, aref.name) &&
                Objects.equals(lastName, aref.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName);
    }
}
