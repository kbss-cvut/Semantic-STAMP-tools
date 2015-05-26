package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.GenericDao;
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
    private GenericDao<EventReport> reportDao;

    @Override
    public EventReport findByKey(String key) {
        return null;
    }

    @Override
    public Collection<EventReport> findAll() {
        return reportDao.findAll(EventReport.class);
    }

    @Override
    public EventReport find(URI uri) {
        return null;
    }

    @Override
    public void persist(EventReport instance) {

    }

    @Override
    public void update(EventReport instance) {

    }

    @Override
    public void remove(EventReport instance) {

    }
}
