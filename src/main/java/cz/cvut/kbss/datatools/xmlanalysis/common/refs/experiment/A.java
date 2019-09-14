package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

public class A {

    protected String id;
    protected String name;
    protected String secondName;

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
}
