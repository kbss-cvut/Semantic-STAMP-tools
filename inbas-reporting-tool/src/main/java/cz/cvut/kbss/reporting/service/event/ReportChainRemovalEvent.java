package cz.cvut.kbss.reporting.service.event;

import java.util.Objects;

/**
 * Event occurring when a report chain is removed.
 */
public class ReportChainRemovalEvent extends AbstractRemovalEvent {

    private final Long fileNumber;

    public ReportChainRemovalEvent(Object source, Long fileNumber) {
        super(source);
        this.fileNumber = Objects.requireNonNull(fileNumber);
    }

    /**
     * Gets file number identifying the removed report chain.
     *
     * @return File number
     */
    public Long getFileNumber() {
        return fileNumber;
    }
}
