package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class InstanceAdapter extends XmlAdapter<InstanceRef, Instance> implements BaseEntity{

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
