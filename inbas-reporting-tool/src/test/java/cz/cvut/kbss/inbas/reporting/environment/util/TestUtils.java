package cz.cvut.kbss.inbas.reporting.environment.util;

import java.lang.reflect.Field;

public class TestUtils {

    private TestUtils() {
        throw new AssertionError();
    }

    /**
     * Sets value of the specified field to the specified value.
     *
     * @param field  The field to set
     * @param target Target on which the field value will be set. Can be {@code null} for static fields
     * @param value  The value to use
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Field field, Object target, Object value) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(target, value);
    }
}
