package cz.cvut.kbss.datatools.bpm2stampo.common.refs.experiment;

import java.util.Objects;

public class A {

    public String id;
    public String name;
    public String secondName;

    public A(String id, String name, String secondName) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "A{" +
                "\n\tid='" + id + '\'' + "," +
                "\n\tname='" + name + '\'' + "," +
                "\n\tsecondName='" + secondName + '\'' + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a = (A) o;
        return Objects.equals(id, a.id) &&
                Objects.equals(name, a.name) &&
                Objects.equals(secondName, a.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, secondName);
    }
}
