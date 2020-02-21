package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.TYPE})
public @interface SetIRI {
    String prefix() default "";
    String[] beforeId() default {};
    String[] afterId() default {};
    String sep() default "-";
    String sourceField() default "";
}
