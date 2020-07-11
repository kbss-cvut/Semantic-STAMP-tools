package cz.cvut.kbss.datatools.bpm2stampo.common.refs.experiment;

import java.util.Arrays;
import java.util.List;

public class TestModel1 {



    public static A a1;
    public static A a2;
    public static A a3;
    public static A a4;
    public static Aref a1ref;
    public static Aref a2ref;
    public static B b1;


    public static List<Object> objects1;


    public static C c1;
    public static List<Object> objects2;

    public static void initObjects(){
        a1 = new A("1", "n1","sn1");
        a2 = new A("2", "n2","sn2");
        a3 = new A("3", "n3","sn3");
        a4 = new A("4", "n4","sn4");
        a1ref = new Aref("n1", "sn1");
        a2ref = new Aref("n2", "sn2");
        b1 = new B("5", a1ref, a2ref);


        objects1 = Arrays.asList(
                a1,a2,a3,
                a1ref,a2ref,
                b1
        );


        c1 = new C("6", Arrays.asList("3","4"), Arrays.asList(a1ref, a2ref));
        objects2 = Arrays.asList(
                a1,a2,a3,a4,
                a1ref,a2ref,
                c1
        );
    }
}
