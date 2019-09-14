package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.IDRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Experiment {
    private static final Logger LOG = LoggerFactory.getLogger(Experiment.class);

    /**
     * Test injection when the FK is defined in a continer class, i.e. a class different from the class which contains
     * the relation field.
     */
    public static void test1(){
        IDRuntime runtime = new IDRuntime();
        Aref a1 = new Aref("n1", "sn1");
        Aref a2 = new Aref("n2", "sn2");
        List objects = Arrays.asList(

                new A("1", "n1","sn1"),
                new A("2", "n2","sn2"),
                new A("3", "n3","sn3"),
                a1,a2,
                new B("4", a1, a2)
        );

        objects.stream().forEach(System.out::println);
        runtime.injectReferences(objects);
        objects.stream().forEach(System.out::println);
    }

    /**
     * Test injection when the FK is defined in the same class as the relation field.
     */
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



    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }

}
