package cz.cvut.kbss.inbas.audit.services;

import cz.cvut.kbss.inbas.audit.test.config.TestServiceConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ledvima1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestServiceConfig.class})
public abstract class BaseServiceTestRunner {
}
