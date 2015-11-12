package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.config.ServiceConfig;
import cz.cvut.kbss.inbas.audit.test.config.TestPersistenceConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, TestPersistenceConfig.class})
public abstract class BaseServiceTestRunner {
}
