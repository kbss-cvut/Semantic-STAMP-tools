package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.Types;

import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public class Identifiable {
    @Id
    protected String iri;

    @Types
    protected Set<String> types = new HashSet<>();

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
