package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import org.apache.jena.rdf.model.Model;

public interface IGraphMLRenderer<T extends GraphMLBuilder> {
    public T render(Model m);


}
