package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel;

public class BaseXMLEntity implements IBaseXMLEntity {
    protected IBaseXMLEntity parent;

    @Override
    public IBaseXMLEntity getParent() {
        return parent;
    }

    @Override
    public void setParent(IBaseXMLEntity parent) {
        this.parent = parent;
    }

    public <T> T getFirstAncestor(Class<T> cls){
        IBaseXMLEntity p = parent;
        while(p != null && !cls.isAssignableFrom(p.getClass())){
            p = p.getParent();
        }
        return (T)p;
    }
}
