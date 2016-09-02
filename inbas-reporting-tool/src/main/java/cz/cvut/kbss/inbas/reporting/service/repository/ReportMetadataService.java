package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.service.security.SecurityUtils;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class ReportMetadataService {

    @Autowired
    private SecurityUtils securityUtils;

    /**
     * Initializes report provenance metadata on the specified instance.
     * <p>
     * I.e. report's author and creation timestamp.
     *
     * @param report Report to initialize with metadata
     */
    public void initReportProvenanceMetadata(AbstractReport report) {
        Objects.requireNonNull(report);
        report.setAuthor(securityUtils.getCurrentUser());
        report.setDateCreated(new Date());
    }

    /**
     * Sets report metadata on the specified instance, which is about to be persisted into the repository.
     * <p>
     * The metadata include report provenance (using {@link #initReportProvenanceMetadata(AbstractReport)}) and
     * identification (key, file number).
     *
     * @param report Report to initialize with metadata
     */
    public void initMetadataForPersist(AbstractReport report) {
        initReportProvenanceMetadata(report);
        report.setFileNumber(IdentificationUtils.generateFileNumber());
        report.setRevision(Constants.INITIAL_REVISION);
    }

    /**
     * Sets report metadata on the specified instance, which is about to be merged into the repository.
     * <p>
     * The metadata include last modifier and timestamp of the modification.
     *
     * @param report Report to update with metadata
     */
    public void initMetadataForUpdate(AbstractReport report) {
        Objects.requireNonNull(report);
        report.setLastModifiedBy(securityUtils.getCurrentUser());
        report.setLastModified(new Date());
    }
}
