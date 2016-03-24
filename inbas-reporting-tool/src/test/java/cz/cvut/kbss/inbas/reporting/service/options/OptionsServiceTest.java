package cz.cvut.kbss.inbas.reporting.service.options;

import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OptionsServiceTest extends BaseServiceTestRunner {

    @Autowired
    private OptionsService optionsService;

    @Test
    public void getOptionsReturnsListOfLVPValues() {
        final Object result = optionsService.getOptions("lvp");
        assertNotNull(result);
        assertTrue(result instanceof List);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOptionsThrowsIllegalArgumentExceptionForUnknownOptionsType() {
        optionsService.getOptions("someUnknownOptionsType");
    }
}