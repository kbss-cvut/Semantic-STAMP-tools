package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.formgen.FormGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/formGen")
public class FormGenController extends BaseController {

    @Autowired
    private FormGenService formGenService;

    @Autowired
    private DtoMapper dtoMapper;

    @RequestMapping(method = RequestMethod.POST)
    public RawJson generateForm(@RequestBody Object data) {
        // TODO Generify mapping of DTOs of unknown type
        if (data instanceof OccurrenceReportDto) {
            return formGenService.generateForm(dtoMapper.occurrenceReportDtoToOccurrenceReport(
                    (OccurrenceReportDto) data));
        }
        return formGenService.generateForm(data);
    }
}
