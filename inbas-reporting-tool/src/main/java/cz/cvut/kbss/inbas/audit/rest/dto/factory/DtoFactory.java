package cz.cvut.kbss.inbas.audit.rest.dto.factory;

import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.model.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventReportDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.RunwayIncursionDto;

/**
 * @author ledvima1
 */
public interface DtoFactory {

    EventReport toDomainModel(EventReportDto dto);

    EventTypeAssessment toDomainModel(RunwayIncursionDto dto);

    EventReportDto toDto(EventReport report);
}
