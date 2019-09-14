package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.generic;

public class Relation<T> extends Typed<T>  {
    protected Element left;
    protected Element right;

    public Element getLeft() {
        return left;
    }

    public void setLeft(Element left) {
        this.left = left;
    }

    public Element getRight() {
        return right;
    }

    public void setRight(Element right) {
        this.right = right;
    }
}
