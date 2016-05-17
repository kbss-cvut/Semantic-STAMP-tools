package cz.cvut.kbss.inbas.reporting.service.formgen;

import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;

public interface FormGenService {

    /**
     * Gets a form structure description for the specified data.
     *
     * @param data Data for the form
     * @return Form structure description in JSON(-LD)
     */
    <T> RawJson generateForm(T data);
}
