package cz.cvut.kbss.reporting.model.util;

import cz.cvut.kbss.reporting.model.LogicalDocument;

import java.util.Comparator;
import java.util.Date;

/**
 * Compares logical documents by date created and revision (if necessary). The resulting order is descending.
 */
public class ReportLastModifiedComparator implements Comparator<LogicalDocument> {
    @Override
    public int compare(LogicalDocument a, LogicalDocument b) {
        assert a.getDateCreated() != null;
        assert b.getDateCreated() != null;
        final Date aLastEdit = a.getLastModified() != null ? a.getLastModified() : a.getDateCreated();
        final Date bLastEdit = b.getLastModified() != null ? b.getLastModified() : b.getDateCreated();
        return bLastEdit.compareTo(aLastEdit);
    }
}
