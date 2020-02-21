package cz.cvut.kbss.datatools.xmlanalysis.common.refs;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations.FIDAttribute;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment.A;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment.Aref;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment.BB;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment.C;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment2.D;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment2.Da;
import cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment2.Db;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment.TestModel1.*;
import static org.junit.Assert.*;

public class ObjectReferenceTest {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectReferenceTest.class);


    /**
     * Test injection when the FK is defined in a continer class, i.e. a class different from the class which contains
     * the relation field.
     */
    @Test
    public void testRelationInjectionTroughReferencedField(){
        IDRuntime runtime = new IDRuntime();
        initObjects();

//        objects.stream().forEach(System.out::println);
        assertNull(b1.a1);
        assertNull(b1.a2);
        runtime.injectReferences(objects1);
        assertNotNull(b1.a1);
        assertNotNull(b1.a2);
        assertEquals(a1, b1.a1);
        assertEquals(a2, b1.a2);
//        objects.stream().forEach(System.out::println);
    }

    @Test
    public void testRelationInjectionTroughReferencedFieldInCollection(){
        IDRuntime runtime = new IDRuntime();
        initObjects();
//        objects.stream().forEach(System.out::println);
        assertNull(c1.refs);
        assertNull(c1.refs2);
        runtime.injectReferences(objects2);
        assertNotNull(c1.refs);
        assertNotNull(c1.refs2);
        assertEquals(2, c1.refs.size());
        assertEquals(2, c1.refs2.size());
        assertTrue(c1.refs2.contains("3"));
        assertTrue(c1.refs2.contains("4"));
        assertTrue(c1.refs2.contains(a1ref));
        assertTrue(c1.refs2.contains(a2ref));
//        objects.stream().forEach(System.out::println);
    }



    @Test
    public void testInjectReferencesForRelationsOfFksOverTheSameField(){
        Class cls = D.class;
        List<Field> allKeyAttributeFields = FieldUtils.getFieldsListWithAnnotation(cls, FIDAttribute.class);

        D d = new D("d1");
        Da da = new Da("d2", "d1");
        Db db = new Db("d3", "d1");
        List<Object> objects = Arrays.asList(d, da, db);
        IDRuntime idRuntime = new IDRuntime();

        idRuntime.injectReferences(objects);

        assertNotNull(d.getDa());
        assertNotNull(d.getDb());
        assertEquals(da, d.getDa());
        assertEquals(db, d.getDb());
    }

    /**
     * Test injection when the FK is defined in the same class as the relation field.
     */
//    @Test
    public static void test2(){
        IDRuntime runtime = new IDRuntime();

        List objects = Arrays.asList(
                new A("1", "n1","sn1"),
                new A("2", "n2","sn2"),
                new A("3", "n3","sn3"),
                new BB("4", "n1", "sn1", "n2", "sn2")
        );
        LOG.info("Printing objects before injection");
        objects.stream().forEach(System.out::println);
        runtime.injectReferences(objects);
        LOG.info("Printing objects after injection");
        objects.stream().forEach(System.out::println);
    }

    /**
     * Test injection when the FK is defined in the same class as the relation field.
     */
    public static void test3(){
        IDRuntime runtime = new IDRuntime();
        Aref a1 = new Aref("n1", "sn1");
        Aref a2 = new Aref("n2", "sn2");
        List objects = Arrays.asList(
                new A("1", "n1","sn1"),
                new A("2", "n2","sn2"),
                new A("3", "n3","sn3"),
//                a1,a2,
                new C("4", Arrays.asList("1","3"), Arrays.asList(a1,a2))
        );
        LOG.info("Printing objects before injection");
        objects.stream().forEach(System.out::println);
        runtime.injectReferences(objects);
        LOG.info("Printing objects after injection");
        objects.stream().forEach(System.out::println);
    }



//    public static void main(String[] args) {
//        test1();
////        test2();
////        test3();
//    }

}
