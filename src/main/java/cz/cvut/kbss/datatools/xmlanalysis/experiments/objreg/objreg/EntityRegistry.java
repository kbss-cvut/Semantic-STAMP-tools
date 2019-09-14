package cz.cvut.kbss.datatools.xmlanalysis.experiments.objreg.objreg;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityRegistry {

    protected Map<String, Object> objectMap;


    public <T> Object registerObject(T t, String ... idAttributes){
        Objects.requireNonNull(t);
        String id = calculate(t, idAttributes);
        return objectMap.put(id, t);
    }

    public <T> T getObject(T t, String ... idAttributes){
        Objects.requireNonNull(t);
        String id = calculate(t, idAttributes);
        return (T)getObject(id, t.getClass());
    }

    public <T> T getObject(String id, Class<T> cls){
        return (T)objectMap.get(id);
    }


    public String calculate(Object obj, String ... idAttributes){
        return  calculate(obj.getClass().getCanonicalName(), idAttributes);
    }
    public String calculate(String str, String ... idAttributes){
        return str + "-" + Stream.of(idAttributes).collect(Collectors.joining());
    }
}
