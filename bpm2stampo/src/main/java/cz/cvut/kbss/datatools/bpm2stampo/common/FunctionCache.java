package cz.cvut.kbss.datatools.bpm2stampo.common;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class FunctionCache<T> {

    protected Map<String, T> cache = new HashMap<>();

    public String constructKey(String ... args){
        return StringUtils.join(args, "-");
    }

    public T get(String ... args){
        String key = constructKey(args);
        return cache.get(key);
    }

    public T set(T val, String ... args){
        String key = constructKey(args);
        return cache.get(key);
    }

    public static void main(String[] args) {
        FunctionCache<String> fc= new FunctionCache<>();
        String key = fc.constructKey("a", "b", "c");
        System.out.println(key);
    }

}
