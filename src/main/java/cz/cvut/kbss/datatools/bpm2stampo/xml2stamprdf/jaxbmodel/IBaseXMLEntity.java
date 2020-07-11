package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.jaxbmodel;

public interface IBaseXMLEntity {
    IBaseXMLEntity getParent();

    void setParent(IBaseXMLEntity parent);
}
