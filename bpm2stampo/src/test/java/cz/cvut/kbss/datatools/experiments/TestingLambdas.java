package cz.cvut.kbss.datatools.experiments;

import java.util.Objects;
import java.util.function.Function;

public class TestingLambdas {

    public static String toString(Object o){
        return String.format("String-value = \"%s\"", Objects.toString(o));
    }

    public void experimentLambdaFromClass(){
        Class<? extends Function> fc = lambdaToClass(TestingLambdas::toString);


    }

    public Class<? extends Function> lambdaToClass(Function f){
        return f.getClass();
    }
}
