package cz.cvut.kbss.datatools.experiments;

import org.junit.Test;

public class ExperimentOverrideDefaultMethodInIterface {
    public static interface A{
        default String method(){
            return "a";
        }
    }

    public static interface B extends A{
        default String method(){
            return "b";
        }
    }


    public static class C implements B{}


    @Test
    public void experimentOverridenDefaultMethod(){
        C c = new C();
        System.out.println(c.method());
    }
}
