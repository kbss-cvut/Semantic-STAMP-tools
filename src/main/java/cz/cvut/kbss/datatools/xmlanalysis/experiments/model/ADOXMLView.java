package cz.cvut.kbss.datatools.xmlanalysis.experiments.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ADOXMLView<T> {
    protected T ref;

    public ADOXMLView() {
    }

    public ADOXMLView(T ref) {
        setRef(ref);
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
        init(ref);
    }

    public void init(T ref){}

    // utility methods
    public <T> List<T> selectElements(Collection elements, Class<T> cls){
        List<T> filteredList = new ArrayList<>();
        for(Object el : elements){
            if(cls.isInstance(el)){
                filteredList.add(cls.cast(el));
            }
        }
        return filteredList;
    }

    public <T> List<T> getModelElements(String clazz, Class<T> targetClass){
        return null;
    }



}
