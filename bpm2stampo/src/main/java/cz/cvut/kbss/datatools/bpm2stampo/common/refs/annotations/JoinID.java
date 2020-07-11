package cz.cvut.kbss.datatools.bpm2stampo.common.refs.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD})
public @interface JoinID {
    String to();
}
