package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.generic;

public abstract class Base<T> {
    protected T source;
    protected CacheableFunction transformer;

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public void transform(){
        transformer.setArg(source);
        Object ret = transformer.evaluate();
    }

}
