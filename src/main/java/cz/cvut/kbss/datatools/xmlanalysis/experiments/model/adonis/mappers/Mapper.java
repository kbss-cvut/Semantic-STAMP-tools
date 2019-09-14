package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.mappers;

public abstract class Mapper<S,T> {
    protected S sourceModel;
    protected T target;

    public S getSourceModel() {
        return sourceModel;
    }

    public void setSourceModel(S sourceModel) {
        this.sourceModel = sourceModel;
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }


}
