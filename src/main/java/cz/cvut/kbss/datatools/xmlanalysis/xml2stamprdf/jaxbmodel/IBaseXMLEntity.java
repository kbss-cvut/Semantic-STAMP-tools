package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.jaxbmodel;

public interface IBaseXMLEntity {
    IBaseXMLEntity getParent();

    void setParent(IBaseXMLEntity parent);
}
