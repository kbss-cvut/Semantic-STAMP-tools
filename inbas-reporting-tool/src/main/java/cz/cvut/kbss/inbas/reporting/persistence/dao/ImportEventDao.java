/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.repo.ImportEvent;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Repository
public class ImportEventDao extends BaseDao<ImportEvent>{
    
    public ImportEventDao() {
        super(ImportEvent.class);
    }
    
}
