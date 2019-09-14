package cz.cvut.kbss.datatools.xmlanalysis.experiments.model;

import cz.cvut.kbss.adoxml.ATTRIBUTE;
import cz.cvut.kbss.adoxml.INSTANCE;
import cz.cvut.kbss.adoxml.INTERREF;
import cz.cvut.kbss.adoxml.RECORD;

import java.util.List;

public class ADOXMLInstanceView extends ADOXMLView<INSTANCE> {

    protected List<ATTRIBUTE> attributes;
    protected List<RECORD> records;
    protected List<INTERREF> interrefs;

    public List<ATTRIBUTE> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ATTRIBUTE> attributes) {
        this.attributes = attributes;
    }

    public List<RECORD> getRecords() {
        return records;
    }

    public void setRecords(List<RECORD> records) {
        this.records = records;
    }

    public List<INTERREF> getInterrefs() {
        return interrefs;
    }

    public void setInterrefs(List<INTERREF> interrefs) {
        this.interrefs = interrefs;
    }

    public String getId() {
        return ref.getId();
    }

    public String getClazz() {
        return ref.getClazz();
    }

    public String getName() {
        return ref.getName();
    }

    @Override
    public void init(INSTANCE ref){
        attributes = selectElements(getRef().getATTRIBUTEOrRECORDOrINTERREF(), ATTRIBUTE.class);
        records = selectElements(getRef().getATTRIBUTEOrRECORDOrINTERREF(), RECORD.class);
        interrefs = selectElements(getRef().getATTRIBUTEOrRECORDOrINTERREF(), INTERREF.class);
    }


}