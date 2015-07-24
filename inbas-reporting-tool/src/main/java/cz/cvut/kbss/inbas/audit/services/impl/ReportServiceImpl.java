package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.ReportDao;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import cz.cvut.kbss.inbas.audit.services.validation.ReportValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author ledvima1
 */
@Service
public class ReportServiceImpl extends BaseService<EventReport> implements ReportService {

    @Autowired
    private ReportValidator reportValidator;

    @Autowired
    private ReportDao reportDao;

    @Override
    protected GenericDao<EventReport> getPrimaryDao() {
        return reportDao;
    }

    @Override
    public EventReport findByKey(String key) {
        return reportDao.findByKey(key);
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
}
