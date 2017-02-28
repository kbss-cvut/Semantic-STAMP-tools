package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole({'" + SecurityConstants.ROLE_GUEST + "', '"
        + SecurityConstants.ROLE_USER + "', '" +
        SecurityConstants.ROLE_ADMIN + "'})")
public class BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

}
