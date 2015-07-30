package cz.cvut.kbss.inbas.audit.rest.dto.factory;

import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReport;

/**
 * @author ledvima1
 */
public interface DtoFactory {

    cz.cvut.kbss.inbas.audit.model.OccurrenceReport toDomainModel(OccurrenceReport dto);

    OccurrenceReport toDto(cz.cvut.kbss.inbas.audit.model.OccurrenceReport report);
}
