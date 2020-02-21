package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.modules;

import java.lang.reflect.Method;

public interface Decorator<T> {

    String[] emptyStringArray = new String[0];

    void configure(Method transformDeclaration);
    void decorate(Object in, Object o);
    T calculateValue(Object in, Object out);
}
