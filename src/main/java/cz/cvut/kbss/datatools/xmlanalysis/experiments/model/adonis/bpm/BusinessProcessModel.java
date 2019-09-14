package cz.cvut.kbss.datatools.xmlanalysis.experiments.model.adonis.bpm;

import cz.cvut.kbss.datatools.xmlanalysis.experiments.model.ADOXMLModelView;

import java.util.HashSet;
import java.util.Set;

public class BusinessProcessModel extends ADOXMLModelView {

    // activities declared in the model
    protected Set<Activity> activities;
    // pools declared in the model
    protected Set<Pool> pools;
    // lanes declared in the model
    protected Set<Lane> lanes;

    public BusinessProcessModel() {
        constructInit();
    }

    private void constructInit(){
        init();
    }

    public void init(){
        activities = new HashSet<>();
        pools = new HashSet<>();
        lanes = new HashSet<>();
    }

    public void clearModel(){
        activities.clear();
        pools.clear();
        lanes.clear();
    }

    public Activity createActivity(){
        Activity act = new Activity();
        activities.add(act);
        return act;
    }

    public boolean add(Activity activity) {
        return activities.add(activity);
    }

    public boolean remove(Activity activity) {
        return activities.remove(activity);
    }

    public boolean add(Pool pool) {
        return pools.add(pool);
    }

    public boolean remove(Pool o) {
        return pools.remove(o);
    }

    public boolean add(Lane lane) {
        return lanes.add(lane);
    }

    public boolean remove(Lane lane) {
        return lanes.remove(lane);
    }

}
