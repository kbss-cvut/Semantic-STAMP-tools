package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.adoxml;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;

public class URIFactory{

    protected String ns;

    public URIFactory(String ns) {
        this.ns = ns;
    }

    public String createURIByClassAndName(String cls, String instanceName) {
        return ns + Utils.urlEncode(cls.trim()) + "-" + Utils.urlEncode(instanceName.trim());
    }

    public String createURIByName(String instanceName) {
        return ns + Utils.urlEncode(instanceName.trim());
    }
}
