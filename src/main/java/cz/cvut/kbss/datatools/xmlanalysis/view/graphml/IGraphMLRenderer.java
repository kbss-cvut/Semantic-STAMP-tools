package cz.cvut.kbss.datatools.xmlanalysis.view.graphml;

import org.apache.jena.rdf.model.Model;
import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.GraphMLBuilder;

public interface IGraphMLRenderer<T extends GraphMLBuilder> {
    public T render(Model m);


}
