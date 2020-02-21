package cz.cvut.kbss.datatools.xmlanalysis.common.refs.experiment2;

import java.util.Arrays;
import java.util.List;

public class TestModel3 {

    public static D d;
    public static Da da;
    public static Db db;
    public static List<Object> objects;


    public static void initObjects(){
        d = new D("d1");
        da = new Da("d2", "d1");
        db = new Db("d3", "d1");
        objects = Arrays.asList(d, da, db);
    }
}
