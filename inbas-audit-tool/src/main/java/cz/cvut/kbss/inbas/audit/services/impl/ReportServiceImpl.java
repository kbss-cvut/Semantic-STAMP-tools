package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.SupportsOwlKey;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import cz.cvut.kbss.inbas.audit.services.validation.ReportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collection;
import java.util.Date;

/**
 * @author ledvima1
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportValidator reportValidator;

    @Autowired
    private SupportsOwlKey<EventReport> reportDao;

    @Override
    public EventReport findByKey(String key) {
        return reportDao.findByKey(EventReport.class, key);
    }

    @Override
    public Collection<EventReport> findAll() {
        return reportDao.findAll(EventReport.class);
    }

    @Override
    public EventReport find(URI uri) {
        return reportDao.findByUri(EventReport.class, uri);
    }

    @Override
    public void persist(EventReport report) {
        reportValidator.validateReport(report);
        report.setCreated(new Date());
        report.setLastEdited(new Date());
        reportDao.persist(report);
    }

    @Override
    public void update(EventReport report) {
        if (report.getUri() == null || report.getKey() == null) {
            throw new IllegalArgumentException("Updated report missing URI or key. " + report);
        }
        reportValidator.validateReport(report);
        report.setLastEdited(new Date());
        reportDao.update(report);
    }

    @Override
    public void remove(EventReport report) {
        reportDao.remove(report);
    }
}
