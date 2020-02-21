package cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This should be used on a Collection field. Each element of the collection is either a Key or it contains a definition of a key.
 */

@Deprecated
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.FIELD})
public @interface ManyToOneFK{

}
