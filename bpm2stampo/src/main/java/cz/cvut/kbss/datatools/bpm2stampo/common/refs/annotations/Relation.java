package cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations;

import cz.cvut.kbss.datatools.bpm2stampo.common.refs.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD})
public @interface Relation {
    String value() default Constants.NO_ID_NAME; // the id of the key (also id of the foreign)

    /**
     * Refers to the name of the field who's type contains the foreign key definition. If the value is not specified or
     * if it is equal to Constants.NO_INSTANCE_REFERENCE, the foreign key definition is searched for in the same class
     * in which the Relation annotation is used.
     * @return
     */
    String instanceRef() default Constants.NO_INSTANCE_REFERENCE; // the Field which contains a key mapping (a pair a foreign key a regular key )

//    String fkName() default Constants.NO_ID_NAME; // NOT IMPLEMENTED
}
