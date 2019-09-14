package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import org.w3c.dom.Node;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransformUtils {

    public static String test(){
        return "test\n";
    }

    public static String hello(String s){
        return String.format("hello %s!\n", s);
    }
    public static String hello(Node n){
        return String.format("hello %s!\n", n.toString());
    }

    public static String stringToURI(String s){
        return String.format("<%s>\n", Utils.urlEncode(s));
    }


    public static String stringToURI(String s1, String s2){
        return String.format("<%s>",
                Stream.of(s1, s2)
                        .map(s -> Utils.urlEncode(s))
                        .collect(Collectors.joining("-"))
        );
    }

}
