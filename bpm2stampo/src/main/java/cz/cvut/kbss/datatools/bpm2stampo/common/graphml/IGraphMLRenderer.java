package cz.cvut.kbss.datatools.bpm2stampo.common.graphml;

import org.apache.jena.rdf.model.Model;

public interface IGraphMLRenderer<T extends GraphMLBuilder> {
    public T render(Model m);


}
