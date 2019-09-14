package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.bpm;

public interface BPMVisitor {

    void visit(BusinessProcessModel bpm);
    void visit(Activity activity);
//    void visit(activity);

}

