package cz.cvut.kbss.inbas.reporting.service.formgen;

import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;

import java.util.Map;

public interface FormGenService {

    /**
     * Gets a form structure description for the specified data.
     *
     * @param data   Data for the form
     * @param params Additional parameters for the form generator
     * @return Form structure description in JSON(-LD)
     */
    <T> RawJson generateForm(T data, Map<String, String> params);
}
