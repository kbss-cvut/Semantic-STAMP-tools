package cz.cvut.kbss.reporting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.reporting.rest.dto.mapper.DtoMapperImpl;
import cz.cvut.kbss.reporting.rest.handler.RestExceptionHandler;
import org.mockito.Spy;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;

public abstract class BaseControllerTestRunner {

    ObjectMapper objectMapper;

    @Spy
    DtoMapper dtoMapper = new DtoMapperImpl();    // This is necessary for the controllers to work

    MockMvc mockMvc;

    public void setUp(BaseController controller) {
        setupObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestExceptionHandler())
                                      .build();
    }

    void setupObjectMapper() {
        this.objectMapper = Environment.getObjectMapper();
    }

    String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    <T> T readValue(MvcResult result, Class<T> targetType) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), targetType);
    }

    <T> T readValue(MvcResult result, TypeReference<T> targetType) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), targetType);
    }

    void verifyLocationEquals(String expectedPath, MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost" + expectedPath, locationHeader);
    }
}
