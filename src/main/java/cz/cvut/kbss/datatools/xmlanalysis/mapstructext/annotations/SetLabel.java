package cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations;

import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target({ElementType.METHOD, ElementType.TYPE})
public @interface SetLabel {
    String propertyIRI() default Vocabulary.s_p_label;
    String[] beforeLabel() default {};
    String sep() default "-";
    String sourceField() default "";
    String targetField() default "";
}
