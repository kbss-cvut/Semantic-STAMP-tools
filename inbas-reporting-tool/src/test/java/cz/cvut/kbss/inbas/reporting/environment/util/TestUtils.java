package cz.cvut.kbss.inbas.reporting.environment.util;

import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.qam.Question;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Consumer;

import static org.junit.Assert.*;

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

    /**
     * Verifies question structure, calling the specified consumer to verify the specified question and then recursively
     * verifying the question's descendants.
     *
     * @param question The root question to verify
     * @param consumer Verification function
     */
    public static void verifyQuestions(Question question, Consumer<Question> consumer) {
        consumer.accept(question);
        question.getSubQuestions().forEach(sq -> verifyQuestions(sq, consumer));
    }

    /**
     * Verifies that corrective measures have been copied.
     * <p>
     * I.e. the collections should have the same size and the copy should contain corrective measures with the same
     * description as the originals. But the instances have to be different and with different identifiers.
     *
     * @param original Original measures
     * @param copy     Copy
     */
    public static void verifyCorrectiveMeasures(Collection<CorrectiveMeasureRequest> original,
                                                Collection<CorrectiveMeasureRequest> copy) {
        assertEquals(original.size(), copy.size());
        boolean found;
        for (CorrectiveMeasureRequest r : original) {
            found = false;
            for (CorrectiveMeasureRequest rr : copy) {
                if (r.getDescription().equals(rr.getDescription())) {
                    found = true;
                    assertNotSame(r, rr);
                    assertNull(rr.getUri());
                }
            }
            assertTrue(found);
        }
    }
}
