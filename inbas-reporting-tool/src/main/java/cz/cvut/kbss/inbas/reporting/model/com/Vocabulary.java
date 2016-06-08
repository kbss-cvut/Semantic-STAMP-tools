/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.com;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Vocabulary {
    public static final String Email = "http://onto.fel.cvut.cz/ontologies/communication-ext/email";
    public static final String ZIP = "http://onto.fel.cvut.cz/ontologies/communication-ext/zip";
    public static final String XML = "http://onto.fel.cvut.cz/ontologies/communication-ext/email";
    
    public static final String hasId = "http://onto.fel.cvut.cz/ontologies/documentation/has-id";
    // Of course the id needs a context in which it is an id. The context is the data source in which the id is considered to be unique
//    public static final String hasId = "http://onto.fel.cvut.cz/ontologies/documentation/has-id";
    public static final String hasFileName = "http://onto.fel.cvut.cz/ontologies/documentation/has-name";
    public static final String hasPart = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_part;
    
    
}
