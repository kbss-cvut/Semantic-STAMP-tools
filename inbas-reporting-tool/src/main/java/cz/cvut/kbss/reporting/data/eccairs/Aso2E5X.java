/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.reporting.data.eccairs;

import cz.cvut.kbss.reporting.model.AbstractEvent;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.qam.Question;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExistsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Transform an OccurrenceReport into a e5x document.
 * Not thread safe.
 * 
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Aso2E5X {
    
    private static final Logger LOG = LoggerFactory.getLogger(Aso2E5X.class);

    public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    protected Document d;

    public Document convert(OccurrenceReport r) throws ParserConfigurationException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();

        d = documentBuilder.newDocument();
        Element root = createSET(r);
        elementCreateOccurrence(root, r);
        return d;
    }
    
    protected Element createSET(OccurrenceReport r){
        Element set = createElement(E5XXmlElement.SET);
        //TaxonomyName="ECCAIRS Aviation" TaxonomyVersion="3.4.0.2" Domain="RIT" Version="1.0.0.0"
        set.setAttribute("TaxonomyName", "ECCAIRS Aviation");
        set.setAttribute("TaxonomyVersion", "3.4.0.2");
        set.setAttribute("Domain", "RIT");
        set.setAttribute("Version", "1.0.0.0");
        d.appendChild(set);
        return set;
    }
    
    protected Element elementCreateOccurrence(Element p, OccurrenceReport r) {
        return new EntityBuilder(p, E5XTerms.Entity.Occurrence){
            @Override
            protected void build() {
                this.addAttribute(E5XTerms.OccurrenceAttribute.Headline, r.getOccurrence().getName());
                // UTC, and local date time
                Date d = r.getOccurrence().getStartTime();
                dateFormat.setTimeZone(TimeZone.getTimeZone("Z"));
                timeFormat.setTimeZone(TimeZone.getTimeZone("Z"));
                this.addAttribute(E5XTerms.OccurrenceAttribute.UTC_Date, dateFormat.format(d));
                this.addAttribute(E5XTerms.OccurrenceAttribute.UTC_Time, timeFormat.format(d));
                dateFormat.setTimeZone(TimeZone.getDefault());
                timeFormat.setTimeZone(TimeZone.getDefault());
                this.addAttribute(E5XTerms.OccurrenceAttribute.Local_Date, dateFormat.format(d));
                this.addAttribute(E5XTerms.OccurrenceAttribute.Local_Time, timeFormat.format(d));

                // TODO - implement event, occurrence class, occurrence category.
                //r.getOccurrence().getFactors().stream().forEach(f -> f.getEvent());

                this.addSubEntity(createNarrative(r));
            }
        }.buildElement();
    }

    protected EntityBuilder createNarrative(OccurrenceReport r){
        return new EntityBuilder(E5XTerms.Entity.Narrative){
            @Override
            protected void build() {
                this.addAttribute(E5XTerms.NarrativeAttribute.Narrative_Language, "12");// TODO - use the value from the question if present. Otherwise do not add this attribute
                this.addAttribute(E5XTerms.NarrativeAttribute.Narrative_Text, r.getSummary(),
                        () -> createElement(E5XXmlElement.PlainText)
                );

            }
        };
    }

    protected EntityBuilder createEvent(String eventTypeId, String narrativeText){
        if(eventTypeId == null || eventTypeId.isEmpty()){
            throw new IllegalArgumentException("Bad input argument eventTypeId when creating an Events entity element builder.");
        }
        return new EntityBuilder(E5XTerms.Entity.Events){
            @Override
            protected void build() {
                this.addAttribute(E5XTerms.EventsAttribute.Event_Type, eventTypeId);
                this.addAttribute(E5XTerms.EventsAttribute.Narrative_Text, narrativeText,
                        () -> createElement(E5XXmlElement.PlainText)
                        );
            }
        };
    }

    protected void extractEccairsEventTypes(AbstractEvent evt){
        // take the event and find it in the eccairs repo.
        // get the value list that it belongs to,
        // based on the value list put the event in the right location in the e5x file:
        // - occurrence category events as OccurrenceAttributes.occurrence_Category
        // - occurrence class events as OccurrenceAttributes.occurrence_Category
        // - event types as events
        //      these will require some mapping knowledge
        //      - if an event type is mapped to have a participant aicraft add it
        //        as link to the aircraft entity. Do the same for other enetities
        //        which allow for links to events
        //

        // get Event type id
        String eventTypeId;

        Question eventTypeQ = findFirstQuestionWithOrigin(evt.getQuestion(), "^http://onto.fel.cvut.cz/ontologies/eccairs/aviation-[^/]+/a-390");
        String codeUri = eventTypeQ.getAnswers().stream().map(a -> a.getCodeValue()).filter(ca -> ca != null).map(uri -> uri.toString()).findFirst().orElse(null);
        Pattern p = Pattern.compile("^http://onto.fel.cvut.cz/ontologies/eccairs/aviation-3.4.0.2/vl-a-390/v-(.+)$");
        Matcher m = p.matcher(codeUri);
        eventTypeId = m.matches() ? m.group(1) : null;

        // get Narrative Text
        String narrativeText;
        Question eventNarattiveQ = findFirstQuestionWithOrigin(evt.getQuestion(), "^http://onto.fel.cvut.cz/ontologies/eccairs/aviation-[^/]+/a-425");
        narrativeText = eventNarattiveQ.getAnswers().stream().map( a -> a.getTextValue()).filter(Objects::isNull).findFirst().orElse(null);


        createEvent(eventTypeId, narrativeText);

//        evt.getTypes().stream().map(et -> {
//
//        }).filter(eventId -> eventId != null).forEach(
//                eventId ->
//                {
//                    // create an event Element in the Occurrence entity element
//
//                }
//        );
    }



    protected Question findFirstQuestionWithOrigin(Question rootQ, String originRegex){
        if(rootQ != null){
            Set<String> types = rootQ.getTypes();
            types = types != null ? types : Collections.emptySet();
            URI questionOriginTemplate = rootQ.getOrigin();
            if(questionOriginTemplate != null){
                String uri = questionOriginTemplate.toString();
                if(uri.matches(originRegex)){
                    return rootQ;
                }
            }
            for(Question q : rootQ.getSubQuestions()){
                findFirstQuestionWithOrigin(q, originRegex);
            }
        }
        return null;
    }



    public class EventTypeHandler{
        protected Pattern eventPattern;

        public EventTypeHandler(Pattern eventPattern) {
            this.eventPattern = eventPattern;
        }

        public void build(){

        }

        public Element buildElement(){

            return null;
        }
    }

    public class EntityBuilder{
        protected E5XTerms.E5XTerm entityTerm;
        protected Element parent;
        protected Element attributes = createElement(E5XXmlElement.ATTRIBUTES);
        protected Element entities = createElement(E5XXmlElement.ENTITIES);
        protected Element links = createElement(E5XXmlElement.LINKS);

        public EntityBuilder(Element parent, E5XTerms.E5XTerm entityTerm) {
            this.entityTerm = entityTerm;
            this.parent = parent;
        }
        public EntityBuilder(E5XTerms.E5XTerm entityTerm) {
            this.entityTerm = entityTerm;
        }

        protected void build(){

        }

        protected Element addAttribute(E5XTerms.E5XTerm attributeTerm, String value){
            return addAttribute(attributeTerm, value, null);
        }
        protected Element addAttribute(E5XTerms.E5XTerm attributeTerm, String value, Supplier<Element> valueElement){
            Element attribute = d.createElementNS(E5XTerms.dataBridgeNS, attributeTerm.getXmlElementName());
            attribute.setAttribute(E5XXmlElement.attributeId.getElementName(), attributeTerm.getEccairsId());
            if(valueElement != null) {
                Element valueEl = valueElement.get();
                valueEl.setTextContent(value);
                attribute.appendChild(valueEl);
            }else{
                attribute.setTextContent(value);
            }
            attributes.appendChild(attribute);
            return attribute;
        }

        protected Element addSubEntity(EntityBuilder builder){
            Element childEntity = builder.buildElement();
            if(childEntity != null) {
                entities.appendChild(childEntity);
            }
            return childEntity;
        }

        public Element buildElement(){
            build();
            final Element entity = d.createElementNS(E5XTerms.dataBridgeNS, entityTerm.getXmlElementName());
            // add entity id
            entity.setAttribute(E5XXmlElement.entityId.getElementName(), entityTerm.getEccairsId());
            // append sections if not empty
            Stream.of(attributes, entities, links).
                    filter(el -> el.getChildNodes().getLength() > 0).
                    forEach(entity::appendChild);
            // attach element to parent
            if(parent != null)
                parent.appendChild(entity);
            return entity;
        }
    }

    protected Element createElement(E5XXmlElement e5xmlElement){
        return d.createElementNS(e5xmlElement.getNamespace(), e5xmlElement.getElementName());
    }
    
    public static void serializeDocument(Document d, String fileName){
        try (FileOutputStream fos = new FileOutputStream(fileName)){ 
            serializeDocument(d, fos);
        } catch (FileNotFoundException ex) {
            LOG.info(String.format("Could not serialize xml document into file\"%s\", file not found.", fileName), ex );
        } catch (IOException ex) {
            LOG.info(String.format("Could not serialize xml document into file\"%s\", an error occured during writing to file.", fileName), ex );
        }
    }

    public static void serializeDocument(Document d, OutputStream os){
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(d);
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            LOG.info("Could not serialize xml document, configuring the transformer to serialize the docuemnt failed", ex);
        } catch (TransformerException ex) {
            LOG.info("Could not serialize xml document, the transformation from DOM to stream failed", ex);
        }
    }

    public static OccurrenceReport createOccurrenceReport(){
        OccurrenceReport report = new OccurrenceReport();
        report.setSummary("summary of the \n mock report.");
        Occurrence occ = new Occurrence();
        occ.setStartTime(new Date());
        occ.setEndTime(new Date(occ.getStartTime().getTime() + 1000));
        occ.setName("This is a mock occurrence!");

        report.setOccurrence(occ);
        return report;
    }

    public static void testE5XExport() throws ParserConfigurationException {
        OccurrenceReport report = createOccurrenceReport();
        Aso2E5X t = new Aso2E5X();
        Document d = t.convert(report);
        Aso2E5X.serializeDocument(d, System.out);
    }
    
    public static void main(String[] args) throws ParserConfigurationException {
        testE5XExport();
    }
}
