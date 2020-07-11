package cz.cvut.kbss.datatools.bpm2stampo.common.refs;



import cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations.JoinID;
import cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model.InstanceRef;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class DefaultProcessorPrototype {

    public List<ObjectJoin> protoExtractAnnotationData(Object inst){
        return protoExtractAnnotationData(inst.getClass());
    }

    public List<ObjectJoin> protoExtractAnnotationData(Class cls){
         List<ObjectJoin> list =  new ArrayList<>();
         for(Method m1 : cls.getDeclaredMethods()){
             JoinID ja = m1.getDeclaredAnnotation(JoinID.class);
             if(ja != null) {
                 for(Method m2 : cls.getDeclaredMethods()){
                     if(m2.getName().equals(ja.to()) && m2.getParameterCount() == 1) { // NOTE : weak condition, may find the wrong method
                         ObjectJoin oj = createJoinDefinition(m1, m2);
                         list.add(oj);
                     }
                 }
             }
         }
         return list;

    }

    protected ObjectJoin createJoinDefinition(Method m1,Method m2){
        ObjectJoin j = new ObjectJoin();
        j.setFrom(m1.getDeclaringClass());
        j.setRefId(m1);
        j.setTo(m2.getParameterTypes()[0]);
        j.setEntityId(m2);
        return j;
    }

    public static class ObjectJoin{
        protected Class from;
        protected Class to;

        protected Method refId;
        protected Method entityId;

        public Class getFrom() {
            return from;
        }

        public void setFrom(Class from) {
            this.from = from;
        }

        public Class getTo() {
            return to;
        }

        public void setTo(Class to) {
            this.to = to;
        }

        public Method getRefId() {
            return refId;
        }

        public void setRefId(Method refId) {
            this.refId = refId;
        }

        public Method getEntityId() {
            return entityId;
        }

        public void setEntityId(Method entityId) {
            this.entityId = entityId;
        }

        @Override
        public String toString() {
            return "ObjectJoin{" +
                    "from=" + from.getCanonicalName() +
                    ", to=" + to.getCanonicalName() +
                    ", refId=" + refId.getName() +
                    ", entityId=" + entityId.getName() +
                    '}';
        }
    }

    public static void main(String[] args) {
        DefaultProcessorPrototype defaultProcessorPrototype = new DefaultProcessorPrototype();
        List<ObjectJoin> l = defaultProcessorPrototype.protoExtractAnnotationData(InstanceRef.class);
        l.forEach(System.out::println);

    }
}
