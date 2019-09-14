package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.generic;

public abstract class CacheableFunction<I, O> {
    protected String name;
    protected I arg;
    protected O cache;

    public CacheableFunction(I arg) {
        this.arg = arg;
    }

    public CacheableFunction() {
    }

    public O getCache() {
        return cache;
    }

    public O resetCache(){
        O ret = cache;
        cache = null;
        return ret;
    }

    public I getArg() {
        return arg;
    }

    public void setArg(I arg) {
        this.arg = arg;
    }

    public O evaluate(I arg){
        setArg(arg);
        return evaluate();
    }

    public O evaluate(){
        if(cache == null){
            cache = evaluateImpl(arg);
        }
        return cache;
    }

    protected abstract O evaluateImpl(I arg);

}
