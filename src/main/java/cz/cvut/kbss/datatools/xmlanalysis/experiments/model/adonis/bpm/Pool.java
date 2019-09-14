package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.bpm;

import java.util.Set;

public class Pool {
    // lanes declared in the model
    protected Set<Lane> lanes;

    public boolean add(Lane lane) {
        return lanes.add(lane);
    }

    public boolean remove(Lane lane) {
        return lanes.remove(lane);
    }
}
