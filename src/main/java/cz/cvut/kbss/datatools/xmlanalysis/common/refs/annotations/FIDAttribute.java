package cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD}) // TODO - implement ElementType.METHOD to use for getters and setters instead of the field
public @interface FIDAttribute {
    String value() default Constants.NO_ID_NAME;
//    String fkId() default Constants.NO_ID_NAME; // NOT IMPLEMENTED // This is used when a class contains multiple sets of fields defining FKs to the same referenced class.
    String fieldRef() default Constants.NO_FIELD_REFERENCE;
    Class cls() default void.class;
}
