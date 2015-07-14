package cz.cvut.kbss.inbas.audit.rest.dto.factory;

import cz.cvut.kbss.inbas.audit.model.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventReport;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion;

/**
 * @author ledvima1
 */
public interface DtoFactory {

    cz.cvut.kbss.inbas.audit.model.EventReport toDomainModel(EventReport dto);

    EventTypeAssessment toDomainModel(RunwayIncursion dto);

    EventReport toDto(cz.cvut.kbss.inbas.audit.model.EventReport report);
}
