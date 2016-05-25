/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model.qam;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Vocabulary {
    private Vocabulary() {
        throw new AssertionError();
    }

    // ------------------------------
    // |            Classes         |
    // ------------------------------

    public static final String Answer = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_c_answer;
    public static final String Question = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_c_question;
//    public static final String Value = "http://onto.fel.cvut.cz/ontologies/documentation/value";
    
    // import metadata
//    public static final String isMappedToASO = "http://onto.fel.cvut.cz/ontologies/integration/integrated";
            
    // ------------------------------
    // |         Properties         |
    // ------------------------------

    public static final String hasValue = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_data_value;// "http://onto.fel.cvut.cz/ontologies/documentation/has_value";
    public static final String hasURIValue = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_object_value;
//    public static final String hasRelationToAnswer = "http://onto.fel.cvut.cz/ontologies/documentation/has_relation_to_answer";
    public static final String hasAnswer = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_answer;
    public static final String hasRelatedQuestion = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_related_question;
//    public static final String hasContext = "http://onto.fel.cvut.cz/ontologies/documentation/has_context";
//    public static final String hasRelationToContext = "http://onto.fel.cvut.cz/ontologies/documentation/has_context";
//    public static final String hasPart = cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_part;
    
}
