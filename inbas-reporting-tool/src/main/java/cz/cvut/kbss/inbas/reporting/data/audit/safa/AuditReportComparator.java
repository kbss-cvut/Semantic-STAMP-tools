/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.data.audit.safa;

import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.SetUtils;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class AuditReportComparator {
    public boolean equals(AuditReport r1, AuditReport r2){
        // use fields : "uri", "dateCreated", "types", "summary"
        // exclude fields
        return  equalsNull(r1, r2) &&
                r1.getUri() == r2.getUri() &&
                r1.getSummary() == r2.getSummary() &&
                r1.getDateCreated() == r2.getDateCreated() &&
                r1.getTypes().equals(r2.getTypes()) &&
                equals(r1.getAudit(), r2.getAudit()) 
//                r1.getAuthor() == r2.getAuthor() &&
//                r1.getRevision() == r2.getRevision() &&
//                r1.getReferences() == r2.getReferences() &&
//                r1.getKey() == r2.getKey() &&
//                r1.getFileNumber() == r2.getFileNumber() &&
//                r1.getLastModified() == r2.getLastModified() &&
//                r1.getLastModifiedBy() == r2.getLastModifiedBy() &&
                ;
                
    }
    
    public boolean equals(Audit a1, Audit a2){
        return equalsNull(a1, a2) &&
                a1.getName() == a2.getName() &&
                a1.getLocation() == a2.getLocation() &&
                a1.getStartDate() == a2.getStartDate() &&
                a1.getEndDate() == a2.getEndDate() &&
                equals(a1.getAuditee(), a2.getAuditee()) &&
                equals(a1.getAuditor(), a2.getAuditor()) &&
                a1.getTypes().equals(a2.getTypes()) &&
                equals(a1.getFindings(), a2.getFindings())
//                a1.getQuestion() == a2.getQuestion() &&
//                a1.getUri() == a2.getUri() &&
                ;
    }
    
    public <T> boolean  equalsNull(T t1, T t2){
        return t1 != null && t2 != null;
    }
    
    public boolean equals(Organization o1, Organization o2){
        return equalsNull(o1, o2) && o1.getUri() == o2.getUri();
    }
    
//    public boolean equals(AuditFinding af1, AuditFinding af2){
//        return equalsNull(af1, af2) &&
//                af1.getDescription() == af2.getDescription() &&
//                af1.getLevel() == af2.getLevel() &&
//                af1.getStatusLastModified() == af2.getStatusLastModified() &&
//                af1.getStatus() == af2.getStatus() &&
//                af1.getTypes() == af2.getTypes() &&
////                af1.getFactors() == af2.getFactors() &&
////                af1.getUri() == af2.getUri() &&
////                af1.getCorrectiveMeasures() == af2.getCorrectiveMeasures() &&
//                ;
//    }
    
    public boolean equals(Set<AuditFinding> afs1, Set<AuditFinding> afs2){
        return auditFindingHash(afs1).equals(auditFindingHash(afs2));
    }
    
    public Set<Integer> auditFindingHash(Set<AuditFinding> afs){
        return afs.stream().map(this::auditFindingHash).collect(Collectors.toSet());
    }
    
    public int auditFindingHash(AuditFinding af){
//        af.getCorrectiveMeasures()
        String auditFindingString = 
                toString(af.getStatusLastModified()) + 
                Optional.of(af.getLevel()).map(l -> l.toString()).orElse("") +
                toString(af.getStatusLastModified()) +
                Objects.toString(af.getDescription(), "") +
                toString(af.getTypes(), x -> x) +
                toString(af.getFactors(), x -> x.toString()) + 
                toString(af.getStatus());
        
        return auditFindingString.hashCode();
    }
    
    public String toString(Date date){
        return Optional.of(date).map(d -> d.getTime() + "").orElse("");
    }
    
    public String toString(URI uri){
        return Optional.of(uri).map(URI::toString).orElse("");
    }
    
    public <T> String toString(Set<T> uris, Function<T,String> f ){
        return Optional.of(uris).map(
                d -> (String)SetUtils.orderedSet(uris).stream().map(f).collect(Collectors.joining(","))
        ).orElse("");
    }
    
    
    public static void main(String[] args) {
//        printFields(AuditReport.class);
//        printFields(Audit.class);
//        printFields(AuditFinding.class);
//        printFields(Organization.class);
//        Field[] fields = Audit.class.getDeclaredFields();
//        System.out.println(fields.length);
//        System.out.println(Stream.of(fields).map(x -> String.format("\"%s\"",x.getName())).collect(Collectors.joining(", ")));
    }
    
    public static void printFields(Class cls){
        printFields(cls, "");
    }
    public static void printFields(Class cls, String depth){
        Method[] methods = cls.getMethods();
        System.out.println(depth + cls.getCanonicalName());
        System.out.println(depth + " - " + Stream.of(methods).filter(m -> m.getName().startsWith("get")).map(x -> String.format("\"%s\"", x.getName())).collect(Collectors.joining(", ")));
        
        String depth2 = depth + "\t";
        
        Class[] classes = cls.getInterfaces();
        Class sclass = cls.getSuperclass();
        Set<Class> allclasses = new HashSet<>(Arrays.asList(classes));
        if(sclass != null){
            allclasses.add(sclass);
        }
        
        for(Class c : allclasses){
            printFields(c, depth2);
        }
    }
    
    public static Set<Class> getClassesTransitive(Class cls, Set<Class> allclasses){
        Class[] classes = cls.getInterfaces();
        Class sclass = cls.getSuperclass();
        if(allclasses == null){
            allclasses = new HashSet<>(Arrays.asList(classes));
        }else{
            allclasses.addAll(Arrays.asList(classes));
        }
        if(sclass != null){
            allclasses.add(sclass);
        }
        for(Class c : classes){
            getClassesTransitive(c, allclasses);
        }
        return allclasses;
    }
}
