package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.generic;

public abstract class Typed<T> extends Base<T> {
    protected Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
