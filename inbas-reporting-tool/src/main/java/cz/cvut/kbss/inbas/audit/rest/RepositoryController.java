package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.persistence.sesame.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secret/repository")
public class RepositoryController {

    @Autowired
    private DataDao dataDao;

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String getData() {
        return dataDao.getRepositoryData();
    }
}
