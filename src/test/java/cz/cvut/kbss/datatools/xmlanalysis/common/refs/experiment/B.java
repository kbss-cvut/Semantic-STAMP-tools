package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.Relation;

import java.util.Objects;

public class B {
    public String id;
    public Aref ref1;
    public Aref ref2;
    @Relation(instanceRef = "ref1")
    public A a1;
    @Relation(instanceRef = "ref2")
    public A a2;

    public B(String id, Aref ref1, Aref ref2) {
        this.id = id;
        this.ref1 = ref1;
        this.ref2 = ref2;
    }

    @Override
    public String toString() {
        return "B{" +
                "\n\tid='" + id + "\'," +
                "\n\tref1=" + ref1 + "," +
                "\n\tref2=" + ref2 + "," +
                "\n\ta1=" + a1 + "," +
                "\n\ta2=" + a2 + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        B b = (B) o;
        return Objects.equals(id, b.id) &&
                Objects.equals(ref1, b.ref1) &&
                Objects.equals(ref2, b.ref2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ref1, ref2);
    }
}
