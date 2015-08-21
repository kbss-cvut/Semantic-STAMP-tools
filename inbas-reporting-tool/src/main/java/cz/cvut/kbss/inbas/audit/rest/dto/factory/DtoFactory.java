package cz.cvut.kbss.inbas.audit.rest.dto.factory;

import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReport;

/**
 * @author ledvima1
 */
public interface DtoFactory {

    cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport toDomainModel(OccurrenceReport dto);
}
