package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment2;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;

public class Db {
    protected String id;
    @FIDAttribute(value = "2", fieldRef = "id", cls = D.class)
    protected String dRefFromDb;


    public Db() {
    }

    public Db(String id) {
        this.id = id;
    }

    public Db(String id, String dRefFromDb) {
        this.id = id;
        this.dRefFromDb = dRefFromDb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdRefFromDb() {
        return dRefFromDb;
    }

    public void setdRefFromDb(String dRefFromDb) {
        this.dRefFromDb = dRefFromDb;
    }
}
