package cz.cvut.kbss.datatools.bpm2stampo.mapstructext;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MapperUtils {
    public static String[] emptyStringArray = new String[0];

    public static String iri(Object o){
        return iri(o, emptyStringArray, emptyStringArray);
    }

    public static String iri(Object obj, String[] beforeId, String[] afterId){
        return null;
    }

    public static Set<String> iris(Collection<? extends Object> inC, String[] beforeId, String[] afterId){
        return Optional.ofNullable(inC)
                .map(c -> c.stream()
                        .filter(o -> o != null)
                        .map(o -> iri(o, beforeId, afterId)).collect(Collectors.toSet()))
                .orElse(Collections.EMPTY_SET);
    }


    public static Set<String> getAndInit(Supplier<Set<String>> get, Consumer<Set<String>> set){
        Set<String> s = get.get();
        if(s == null){
            s = new HashSet<>();
            set.accept(s);
        }
        return s;
    }
}
