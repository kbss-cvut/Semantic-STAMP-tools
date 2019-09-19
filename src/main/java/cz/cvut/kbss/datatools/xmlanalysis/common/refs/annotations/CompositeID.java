package cz.cvut.kbss.datatools.xmlanalysis.common.refs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.TYPE})
public @interface CompositeID {
}
