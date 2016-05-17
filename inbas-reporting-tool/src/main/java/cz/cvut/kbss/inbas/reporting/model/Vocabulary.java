package cz.cvut.kbss.inbas.reporting.model;

public class Vocabulary {

    private Vocabulary() {
        throw new AssertionError();
    }

    // ------------------------------
    // |            Classes         |
    // ------------------------------

    public static final String LogicalRecord = "http://onto.fel.cvut.cz/ontologies/documentation/logical_record";
    public static final String LogicalDocument = "http://onto.fel.cvut.cz/ontologies/documentation/logical_document";
    public static final String Report = "http://onto.fel.cvut.cz/ontologies/documentation/report";
    public static final String OccurrenceReport = "http://onto.fel.cvut.cz/ontologies/documentation/occurrence_report";

    public static final String Event = "http://onto.fel.cvut.cz/ontologies/ufo/Event";
    public static final String Occurrence = "http://onto.fel.cvut.cz/ontologies/aviation-safety/Occurrence";
    public static final String Factor = "http://onto.fel.cvut.cz/ontologies/aviation#Factor";

    public static final String Contribution = "http://onto.fel.cvut.cz/ontologies/safety-management/contribution";
    public static final String Cause = "http://onto.fel.cvut.cz/ontologies/safety-management/cause";
    public static final String Mitigation = "http://onto.fel.cvut.cz/ontologies/safety-management/mitigation";
    public static final String Prevention = "http://onto.fel.cvut.cz/ontologies/safety-management/prevention";

    public static final String CorrectiveMeasureRequest = "http://onto.fel.cvut.cz/ontologies/documentation/corrective_measure_request";
    public static final String CorrectiveMeasure = "http://onto.fel.cvut.cz/ontologies/documentation/aviation-safety-documents/corrective_measure";
    public static final String Request = "http://onto.fel.cvut.cz/ontologies/aviation-safety/request";

    public static final String Object = "http://onto.fel.cvut.cz/ontologies/ufo/Object";
    public static final String Agent = "http://onto.fel.cvut.cz/ontologies/ufo/Agent";
    public static final String Organization = "http://onto.fel.cvut.cz/ontologies/ufo/Organization";
    public static final String Person = "http://onto.fel.cvut.cz/ontologies/ufo/Person";

    // ---------------------------------
    // |            Properties         |
    // ---------------------------------

    public static final String p_documents = "http://onto.fel.cvut.cz/ontologies/documentation/documents";
    public static final String p_hasPart = "http://onto.fel.cvut.cz/ontologies/ufo/has_part";
    public static final String p_hasAuthor = "http://onto.fel.cvut.cz/ontologies/documentation/has-author";
    public static final String p_hasCorrectiveMeasure = "http://onto.fel.cvut.cz/ontologies/aviation/core#has-corrective-measure";//this is goining to be moved to a safety-management ontology
    public static final String p_hasFactor = "http://onto.fel.cvut.cz/ontologies/aviation-safety/has_factor";// this is goining to be moved to a safety-management ontology
    
    // see in the classes, under the Factor class
    public static final String p_factorType = "http://onto.fel.cvut.cz/ontologies/documentation/factor-type"; 
    // I assume that the value for this properties are taken from the query occurrenceClass.sparql. This is however the type of the occurrence.
    // Is it possible to do this using the @Types annotation, for example :
    // @Types
    // private String occurrenceClass;
    // or
    // @Types
    // private String severityAssessment;
    public static final String p_severityAssessment = "http://onto.fel.cvut.cz/ontologies/documentation/severity-assessment";

    // Currently, responsible agent can be person or organization and since JOPA does not support inheritance, we have to specify
    // them explicitly, thus the has-responsible-person and has-responsible-organization properties
//    public static final String p_hasResponsibleAgent = "http://onto.fel.cvut.cz/ontologies/documentation/has-responsible-agent";
    // BK : responsible for what? for example: for the realization of the Corrective Measure or for the design of specification. Corrective Measure is a specification. This kind of property should be used in a request where an Agent is assigned a Task.
    public static final String p_hasResponsiblePerson = "http://onto.fel.cvut.cz/ontologies/documentation/has-responsible-person";
    public static final String p_hasResponsibleOrganization = "http://onto.fel.cvut.cz/ontologies/documentation/has-responsible-organization"; 
    // Same as above
    public static final String p_basedOn = "http://onto.fel.cvut.cz/ontologies/documentation/based-on";
    // I preffer using the same property. based on
    public static final String p_basedOnEvent = "http://onto.fel.cvut.cz/ontologies/documentation/based-on-event";
    public static final String p_basedOnOccurrence = "http://onto.fel.cvut.cz/ontologies/documentation/based-on-occurrence";

    public static final String p_hasKey = "http://onto.fel.cvut.cz/ontologies/documentation/has-key";
    public static final String p_fileNumber = "http://onto.fel.cvut.cz/ontologies/documentation/file-number";

    /**
     * Although instances of EventType should be subclasses of Event, so that their instances could exist (e.g.
     * RunwayIncursion), programming language do not allow a class to be at the same time instance of another class, so
     * we need to connect event instances with event type instances by an attribute.
     */
    // BK: Consider this: this is a question. This question has a model.
    // Alignment with ARMS. The model of this question describes this property as the type of the occurrence.
    public static final String p_hasEventType = "http://onto.fel.cvut.cz/ontologies/documentation/has-event-type";
    /**
     * rdfs:label
     */
    public static final String p_name = "http://www.w3.org/2000/01/rdf-schema#label"; // rdfs:label
    /**
     * rdfs:comment
     */
    public static final String p_comment = "http://www.w3.org/2000/01/rdf-schema#comment"; // rdfs:comment
    public static final String p_description = "http://purl.org/dc/terms/description";
    public static final String p_dateCreated = "http://purl.org/dc/terms/created";
    // or
    //http://onto.fel.cvut.cz/ontologies/aviation/core#has-createion-time
   
    // the following two properties can be infered
    public static final String p_lastModified = "http://onto.fel.cvut.cz/ontologies/aviation/core#last-edit-date";
    public static final String p_lastModifiedBy = "http://onto.fel.cvut.cz/ontologies/aviation/documentation#has-last-editor";
    public static final String p_revision = "http://onto.fel.cvut.cz/ontologies/documentation/revision";

    // I think this should some top level ontology, e.g. ufo, time ontology.
    public static final String p_startTime = "http://onto.fel.cvut.cz/ontologies/aviation/core#has-start-time";
    public static final String p_endTime = "http://onto.fel.cvut.cz/ontologies/aviation/core#has-end-time";

    public static final String p_firstName = "http://xmlns.com/foaf/0.1/firstName";
    public static final String p_lastName = "http://xmlns.com/foaf/0.1/lastName";
    public static final String p_username = "http://xmlns.com/foaf/0.1/accountName";
    public static final String p_password = "http://onto.fel.cvut.cz/ontologies/documentation/password";

    public static final String p_childIndex = "http://onto.fel.cvut.cz/ontologies/inbas-2015/child-index";

    // ARMS Attributes
    // the hipothethical consiquence of events unfold in this occurrence.
    // should mapped to a hipothethical event that could have happened but it didn't for some reasone
    public static final String p_mostProbableAccidentOutcome = "http://onto.fel.cvut.cz/ontologies/documentation/arms-most-probable-accident-outcome";
    // the barier effectiveness should be assessed for a particular event, not only for the occurrence.
    public static final String p_barrierEffectiveness = "http://onto.fel.cvut.cz/ontologies/documentation/arms-barrier-effectiveness";

    // Factor types (for now)
    public static final String p_mitigates = "http://onto.fel.cvut.cz/ontologies/aviation-safety/mitigates";
    public static final String p_causes = "http://onto.fel.cvut.cz/ontologies/aviation-safety/causes";
    public static final String p_contributesTo = "http://onto.fel.cvut.cz/ontologies/aviation-safety/contributes-to";
    public static final String p_prevents = "http://onto.fel.cvut.cz/ontologies/aviation/core#prevents";
}
