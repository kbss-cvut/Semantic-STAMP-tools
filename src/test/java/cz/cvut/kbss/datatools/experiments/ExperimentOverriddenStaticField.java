package cz.cvut.kbss.datatools.experiments;

import org.junit.Assert;
import org.junit.Test;

public class ExperimentOverriddenStaticField {

    @Test
    public void experiment1(){
        B.setVal("b");
//        Assert.assertNull(A.getVal());
        Assert.assertNull(C.getVal());

    }

    public static class A{
        public static String val;

        public static String getVal() {
            return val;
        }

        public static void setVal(String val) {
            A.val = val;
        }
    }


    public static class B extends A{
        public static String a;
        public static String getVal() {
            return val;
        }

        public static void setVal(String val) {
            A.val = val;
        }
    }

    public static class C extends A{
        public static String a;
        public static String getVal() {
            return val;
        }

        public static void setVal(String val) {
            A.val = val;
        }
    }
}
