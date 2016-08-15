package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.service.ReportBusinessService;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceMapper {

    @Autowired
    private ReportBusinessService reportService;

    public <T extends LogicalDocument> T resolve(ReportDto reference, @TargetType Class<T> entityClass) {
        if (reference == null) {
            return null;
        }
        return reportService.findByKey(reference.getKey());
    }
}
