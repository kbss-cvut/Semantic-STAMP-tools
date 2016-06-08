/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.inbas.reporting.model.com.EMail;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EmailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class EMailService {
    
    @Autowired
    protected EmailDao emailDao;
    
    public EMail getMailById(String id){
        return emailDao.findByMailId(id);
    }
    
    public void persist(EMail email){
        emailDao.persist(email);
    }
    
}
