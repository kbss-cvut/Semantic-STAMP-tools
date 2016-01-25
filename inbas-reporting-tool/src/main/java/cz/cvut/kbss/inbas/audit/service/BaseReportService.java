package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.model.reports.Report;

public interface BaseReportService<T extends Report> extends BaseService<T> {

    /**
     * Finds report with the specified key.
     *
     * @param key Key identifier
     * @return Matching report or {@code null}
     */
    T findByKey(String key);

    /**
     * Creates new revision of the specified report.
     *
     * @param report The report to create new revision from
     * @return The new revision instance
     */
    T createNewRevision(T report);
}
