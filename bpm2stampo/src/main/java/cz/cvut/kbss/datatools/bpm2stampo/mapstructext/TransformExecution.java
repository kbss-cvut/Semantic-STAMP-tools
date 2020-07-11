package cz.cvut.kbss.datatools.bpm2stampo.mapstructext;

public class TransformExecution {

    protected Transformer transformer;
    protected Object in;
    protected Object out;

    public TransformExecution() {
    }

    public TransformExecution(Transformer transformer, Object in) {
        this.transformer = transformer;
        this.in = in;
    }

    public TransformExecution(Transformer transformer, Object in, Object out) {
        this.transformer = transformer;
        this.in = in;
        this.out = out;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public Object getIn() {
        return in;
    }

    public void setIn(Object in) {
        this.in = in;
    }

    public Object getOut() {
        return out;
    }

    public void setOut(Object out) {
        this.out = out;
    }

}
