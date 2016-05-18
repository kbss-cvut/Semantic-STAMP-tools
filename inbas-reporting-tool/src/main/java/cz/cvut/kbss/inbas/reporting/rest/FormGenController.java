package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.GenericMapper;
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
    private GenericMapper mapper;

    @RequestMapping(method = RequestMethod.POST)
    public RawJson generateForm(@RequestBody Object data) {
        return formGenService.generateForm(mapper.map(data));
    }
}
