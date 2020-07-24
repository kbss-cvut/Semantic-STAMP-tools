package cz.cvut.kbss.datatools.bpm2stampo.converters.bizagi.model;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.Relation;
import org.eclipse.persistence.oxm.annotations.XmlPath;

public class Event extends BaseEntity {

    @XmlPath("@Trigger")
    protected String trigger;

    @FIDAttribute(fieldRef = "id", cls=Activity.class)
    @XmlPath("@Target")
    protected String target;

    @XmlPath("@IsAttached")
    protected boolean isAttached;


    @Relation
    protected Activity parentActivity;

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean attached) {
        isAttached = attached;
    }
}
