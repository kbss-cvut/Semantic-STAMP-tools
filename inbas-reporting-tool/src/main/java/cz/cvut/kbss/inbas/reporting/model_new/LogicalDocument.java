package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.inbas.reporting.model_new.util.HasOwlKey;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;

import java.util.Date;

/**
 * This interface represents a workaround for JOPA's lack of support for inheritance.
 * <p>
 * It enables the application to handle different kinds of reports in the same manner using some generic service. The
 * interface defines getters which all report entities have to implement, because they represent attributes common to
 * all reports. In the future, the interface should be replaced with a proper parent class, which would declare the
 * corresponding fields.
 */
public interface LogicalDocument extends HasOwlKey, HasUri {

    /**
     * Gets number under which this report is filed.
     * <p>
     * File numbers are used to identify report chains - different revisions of the same report.
     *
     * @return File number
     */
    public Long getFileNumber();

    /**
     * Gets author of the report.
     *
     * @return Author
     */
    public Person getAuthor();

    /**
     * Gets date when this report was created.
     *
     * @return Creation date
     */
    public Date getDateCreated();

    /**
     * Gets date when this report was last modified.
     * <p>
     * If the report has not been modified yet, this method returns {@code null}, it does <b>not</b> return the creation
     * date.
     *
     * @return Date of last modification (if present)
     */
    public Date getLastModified();

    /**
     * Gets author of the last modification of this report.
     * <p>
     * If the report has not been modified yet, this method returns {@code null}, it does <b>not</b> return the report
     * author.
     *
     * @return Author of last modification
     */
    public Person getLastModifiedBy();

    /**
     * Gets revision number of this report.
     * <p>
     * Revision numbers start at one and are integers.
     *
     * @return Revision number
     */
    public Integer getRevision();
}
