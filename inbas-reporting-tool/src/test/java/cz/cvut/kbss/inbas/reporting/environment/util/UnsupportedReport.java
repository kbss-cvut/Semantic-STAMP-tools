package cz.cvut.kbss.inbas.reporting.environment.util;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.Person;

import java.net.URI;
import java.util.Date;

/**
 * Report type not supported by the application.
 */
public class UnsupportedReport implements LogicalDocument {
    @Override
    public Long getFileNumber() {
        return null;
    }

    @Override
    public void setFileNumber(Long fileNumber) {
    }

    @Override
    public Person getAuthor() {
        return null;
    }

    @Override
    public Date getDateCreated() {
        return null;
    }

    @Override
    public Date getLastModified() {
        return null;
    }

    @Override
    public Person getLastModifiedBy() {
        return null;
    }

    @Override
    public Integer getRevision() {
        return null;
    }

    @Override
    public ReportDto toReportDto() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {
    }

    @Override
    public URI getUri() {
        return null;
    }
}
