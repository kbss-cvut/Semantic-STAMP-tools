package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD})
public @interface AddTypesFromVal {
    String field();
    String namespace();
}
