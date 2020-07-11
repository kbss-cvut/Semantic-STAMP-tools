package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@Deprecated
public class InstanceAdapter extends XmlAdapter<InstanceRef, Instance> {

//    protected

    @Override
    public Instance unmarshal(InstanceRef v) throws Exception {
        return null;
    }

    @Override
    public InstanceRef marshal(Instance v) throws Exception {
        return null;
    }
}
