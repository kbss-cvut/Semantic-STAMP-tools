package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.SupportsOwlKey;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collection;

/**
 * @author ledvima1
 */
@Service
public class ReportServiceImpl implements ReportService {

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
    public void persist(EventReport instance) {
        reportDao.persist(instance);
    }

    @Override
    public void update(EventReport instance) {

    }

    @Override
    public void remove(EventReport instance) {

    }
}
