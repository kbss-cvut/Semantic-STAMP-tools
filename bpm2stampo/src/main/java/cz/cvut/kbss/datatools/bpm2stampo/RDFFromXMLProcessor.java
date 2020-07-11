package cz.cvut.kbss.datatools.bpm2stampo;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class RDFFromXMLProcessor extends XMLProcessor {

    @Override
    public void process(Document doc) {

    }

    @Override
    public void postProcess() {
        // NO OP
    }



    public static class RDFGraphBuilder{

        // xpath definitions
        // from document root
        // from context node
        // from processed node


        // define extractions for resource creation
        // xpath for a node representing/containing information about a resource - each node is processed
        //

        protected Node context;
        protected Model model;
//        protected Model metadata = ModelFactory.createDefaultModel();

        public RDFGraphBuilder(Node context) {
            this(context, ModelFactory.createDefaultModel());
        }

        public RDFGraphBuilder(Node context, Model model) {
            this.context = context;
            this.model = model;
        }

        public Node getContext() {
            return context;
        }

        public void setContext(Node context) {
            this.context = context;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public Model process(){
            return null;
        }
    }

    public static class ResourceBuilder{
        protected Model model;
        protected Node node;

        public ResourceBuilder(Model model, Node node) {
            this.model = model;
            this.node = node;
        }
    }
}
