package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.Relation;

public class B {
    protected String id;
    protected Aref ref1;
    protected Aref ref2;
    @Relation(instanceRef = "ref1")
    protected A a1;
    @Relation(instanceRef = "ref2")
    protected A a2;

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
}
