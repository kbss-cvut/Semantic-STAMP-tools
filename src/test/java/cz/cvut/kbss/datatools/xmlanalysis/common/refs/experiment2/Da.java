package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment2;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;

public class Da {

    protected String id;
    @FIDAttribute(value = "1", fieldRef = "id", cls = D.class)
    protected String dRefFromDa;

    public Da() {
    }

    public Da(String id) {
        this.id = id;
    }

    public Da(String id, String dRefFromDa) {
        this.id = id;
        this.dRefFromDa = dRefFromDa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getdRefFromDa() {
        return dRefFromDa;
    }

    public void setdRefFromDa(String dRefFromDa) {
        this.dRefFromDa = dRefFromDa;
    }
}
