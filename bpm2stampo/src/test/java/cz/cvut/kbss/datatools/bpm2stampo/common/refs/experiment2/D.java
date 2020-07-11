package cz.cvut.kbss.datatools.bpm2stampo.common.refs.experiment2;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.FarRelation;

public class D {



    protected String id;

    @FIDAttribute(value = "3", fieldRef = "dRefFromDb", cls = Db.class)
    protected String id1;

    @FarRelation(value = "1")
    protected Da da;

    @FarRelation(value = "2")
    protected Db db;

    public D() {
    }

    public D(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public Da getDa() {
        return da;
    }

    public void setDa(Da da) {
        this.da = da;
    }

    public Db getDb() {
        return db;
    }

    public void setDb(Db db) {
        this.db = db;
    }
}
