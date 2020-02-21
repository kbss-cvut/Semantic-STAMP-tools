package cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations;

import cz.cvut.kbss.datatools.xmlanalysis.common.refs.Constants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This used when the relation is on the far side of the FK declaration using the FIDAttribute annotation
 */
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD})
public @interface FarRelation {
    String value() default Constants.NO_ID_NAME; // the id of the key (also id of the foreign)
}
