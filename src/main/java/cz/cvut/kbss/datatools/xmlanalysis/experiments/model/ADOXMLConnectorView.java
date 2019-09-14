package cz.cvut.kbss.datatools.xmlanalysis.experiments.model;

import cz.cvut.kbss.adoxml.*;

import java.util.List;

public class ADOXMLConnectorView extends ADOXMLView<CONNECTOR> {


    protected List<ATTRIBUTE> attributes;
    protected List<RECORD> records;
    protected List<INTERREF> interrefs;

    public List<ATTRIBUTE> getAttributes() {
        return attributes;
    }

    public List<RECORD> getRecords() {
        return records;
    }

    public List<INTERREF> getInterrefs() {
        return interrefs;
    }

    public String getId() {
        return ref.getId();
    }

    public String getClazz() {
        return ref.getClazz();
    }

    @Override
    public void init(CONNECTOR ref){
        attributes = selectElements(getRef().getATTRIBUTEOrRECORDOrINTERREF(), ATTRIBUTE.class);
        records = selectElements(getRef().getATTRIBUTEOrRECORDOrINTERREF(), RECORD.class);
        interrefs = selectElements(getRef().getATTRIBUTEOrRECORDOrINTERREF(), INTERREF.class);
    }
}
