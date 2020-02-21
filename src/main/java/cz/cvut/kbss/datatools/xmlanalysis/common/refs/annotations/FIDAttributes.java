package cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD}) // TODO - implement ElementType.METHOD to use for getters and setters instead of the field
public @interface FIDAttributes {
    FIDAttribute[] value();
}
