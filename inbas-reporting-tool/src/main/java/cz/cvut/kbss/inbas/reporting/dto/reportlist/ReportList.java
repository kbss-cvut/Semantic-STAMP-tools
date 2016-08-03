package cz.cvut.kbss.inbas.reporting.dto.reportlist;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Workaround to the type erasure issue in Java.
 * <p>
 * When a polymorphic collection is supposed to be serialized, the serializer does not have access to the {@link
 * com.fasterxml.jackson.annotation.JsonTypeInfo}.
 *
 * @see <a href="https://github.com/FasterXML/jackson-databind/issues/336">https://github.com/FasterXML/jackson-databind/issues/336</a>
 */
public class ReportList extends ArrayList<ReportDto> {

    public ReportList() {
    }

    public ReportList(int initialCapacity) {
        super(initialCapacity);
    }

    public ReportList(Collection<ReportDto> all) {
        super(all);
    }
}
