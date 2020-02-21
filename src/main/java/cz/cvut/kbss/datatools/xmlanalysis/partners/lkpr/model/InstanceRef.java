package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.JoinID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


@XmlAccessorType(XmlAccessType.FIELD)
public class InstanceRef extends BaseEntity{
    public static final String C = Instance.class.getName();

    @FIDAttribute(cls = Instance.class)
    @XmlAttribute(name = "class")
    protected String cls;

    @FIDAttribute(fieldRef = "name")
    @XmlAttribute
    protected String instance;

    public InstanceRef() {
    }

    public InstanceRef(String cls, String instance) {
        this.cls = cls;
        this.instance = instance;
    }

    public String getInstance() {
        return instance;
    }

    public String getCls() {
        return cls;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "InstanceRef{" +
                "name='" + instance + '\'' +
                ", cls='" + cls + '\'' +
                '}';
    }

    // custome identifiers:


//    public Instance getReferenced() {
//        return referenced;
//    }
//
//    public void setReferenced(Instance referenced) {
//        this.referenced = referenced;
//    }

    public String instanceID(Instance instance){
        return constructId(instance.getName(), instance.getCls());
    }

    @JoinID(to = "instanceID")
    public String instanceRefID(){
        return constructId(getInstance(), getCls());
    }

    public String constructId(String name, String cls){
        return name + cls;
    }

//    @XmlRootElement(name = "FROM")
//    @XmlAccessorType(XmlAccessType.PROPERTY)
//    public static class From{
//        protected String instance;
//        protected String cls;
//
//        @XmlAttribute
//        public String getInstance() {
//            return instance;
//        }
//
//        @XmlAttribute(name = "class")
//        public String getCls() {
//            return cls;
//        }
//
//        public void setInstance(String instance) {
//            this.instance = instance;
//        }
//
//        public void setCls(String cls) {
//            this.cls = cls;
//        }
//
//        @Override
//        public String toString() {
//            return "InstanceRef{" +
//                    "name='" + instance + '\'' +
//                    ", cls='" + cls + '\'' +
//                    '}';
//        }
//    }
//
//    @XmlRootElement(name = "To")
//    public static class To extends InstanceRef{
//
//    }


}
