/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.model_new.qam;

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

    public static final String Answer = "http://onto.fel.cvut.cz/ontologies/documentation/logical_record";
    public static final String Question = "http://onto.fel.cvut.cz/ontologies/documentation/question";
    public static final String Value = "http://onto.fel.cvut.cz/ontologies/documentation/value";
    
    // ------------------------------
    // |         Properties         |
    // ------------------------------

    public static final String hasValue = "http://onto.fel.cvut.cz/ontologies/documentation/has_value";
    public static final String hasRelationToAnswer = "http://onto.fel.cvut.cz/ontologies/documentation/has_relation_to_answer";
    public static final String hasAnswer = "http://onto.fel.cvut.cz/ontologies/documentation/has_answer";
    public static final String hasRelatedQuestion = "http://onto.fel.cvut.cz/ontologies/documentation/has_related_question";
    public static final String hasContext = "http://onto.fel.cvut.cz/ontologies/documentation/has_context";
    public static final String hasRelationToContext = "http://onto.fel.cvut.cz/ontologies/documentation/has_context";
    public static final String hasPart = "http://onto.fel.cvut.cz/ontologies/ufo/has_part";
    
}
