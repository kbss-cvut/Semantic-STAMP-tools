package cz.cvut.kbss.inbas.audit.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import cz.cvut.kbss.inbas.audit.exception.JsonLdTransformationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Transforms JSON-LD result of SPARQL query for event types into an easier to use and read plain JSON.
 * <p>
 * The reason for this is that the received JSON-LD contains keys like "http://www.w3.org/2000/01/rdf-schema#comment",
 * which are valid, but unusable for JavaScript as object attribute names. This transformer uses the Jackson streaming
 * API to transform it to a plain JSON object with simple names, so that "@id" becomes "id" and
 * "http://www.w3.org/2000/01/rdf-schema#comment" becomes "comment".
 */
public class EventTypeJsonLdTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(EventTypeJsonLdTransformer.class);

    private static final String COMMENT_KEY = "http://www.w3.org/2000/01/rdf-schema#comment";
    private static final String LABEL_KEY = "http://www.w3.org/2000/01/rdf-schema#label";
    private static final String DESCRIPTION_KEY = "description";
    private static final String NAME_KEY = "name";
    private static final String TYPE_KEY = "type";

    private boolean comment;
    private boolean label;
    private boolean inArray;
    private List<String> arrayValues;
    private boolean inObject;
    private String currentKey;

    private final JsonFactory jsonFactory;

    public EventTypeJsonLdTransformer() {
        this.jsonFactory = new JsonFactory();
    }

    private void reset() {
        this.comment = false;
        this.label = false;
        this.inArray = false;
        this.inObject = false;
        this.currentKey = null;
    }

    /**
     * Transforms JSON-LD input into a more user-friendly JSON. The rules are:
     * <p>
     * <ul> <li>@attribute -> attribute</li> <li>{@link #COMMENT_KEY} -> description</li> <li>{@link #LABEL_KEY} ->
     * name</li> </ul>
     * <p>
     * In addition, the comment and label fields in the JSON-LD are arrays of objects with a @value, but they are
     * transformed to simple text-valued fields. The type array stays as an array.
     *
     * @param jsonLd The JSON-LD string to transform
     * @return A JSON serialized in string
     */
    public String transform(String jsonLd) {
        reset();
        try (final ByteArrayOutputStream generatorOutput = new ByteArrayOutputStream();
             final JsonGenerator generator = jsonFactory.createGenerator(generatorOutput);
             final JsonParser parser = jsonFactory.createParser(jsonLd)) {

            processJsonLd(parser, generator);
            generator.flush();
            return generatorOutput.toString();
        } catch (IOException e) {
            throw new JsonLdTransformationException(e);
        }
    }

    private void processJsonLd(JsonParser parser, JsonGenerator generator) throws IOException {
        generator.writeStartArray();
        JsonToken token;
        while ((token = parser.nextToken()) != null) {
            switch (token) {
                case START_OBJECT:
                    startObject(generator);
                    break;
                case END_OBJECT:
                    endObject(generator);
                    break;
                case START_ARRAY:
                    startArray();
                    break;
                case END_ARRAY:
                    endArray(generator);
                    break;
                case FIELD_NAME:
                    resolveKey(parser.getCurrentName());
                    break;
                case VALUE_STRING:
                    writeValue(generator, parser);
                    break;
                default:
                    break;
            }
        }
        generator.writeEndArray();
    }

    private void startObject(JsonGenerator generator) throws IOException {
        if (!inObject) {
            generator.writeStartObject();
            this.inObject = true;
        }
    }

    private void startArray() throws IOException {
        if (currentKey != null && currentKey.equals(TYPE_KEY)) {
            this.inArray = true;
            this.arrayValues = new ArrayList<>();
        }
    }

    private void resolveKey(String name) {
        if (name.equals(COMMENT_KEY)) {
            this.comment = true;
            this.currentKey = DESCRIPTION_KEY;
            return;
        } else if (name.equals(LABEL_KEY)) {
            this.label = true;
            this.currentKey = NAME_KEY;
            return;
        }
        if (!comment && !label && name.startsWith("@")) {
            this.currentKey = name.substring(1);
        }
    }

    private void writeValue(JsonGenerator generator, JsonParser parser) throws IOException {
        if (inArray) {
            arrayValues.add(parser.getValueAsString());
        } else {
            generator.writeStringField(currentKey, parser.getValueAsString());
        }
    }

    private void endArray(JsonGenerator generator) throws IOException {
        if (currentKey.equals(TYPE_KEY)) {
            this.inArray = false;
            if (arrayValues.isEmpty()) {
                return;
            }
            if (arrayValues.size() > 1) {
                LOG.warn("Type array size is greater than 1, using the first value. Values: " + arrayValues);
            }
            generator.writeStringField(currentKey, arrayValues.get(0));
        }
    }

    private void endObject(JsonGenerator generator) throws IOException {
        if (comment) {
            this.comment = false;
        } else if (label) {
            this.label = false;
        } else {
            generator.writeEndObject();
            this.inObject = false;
        }
    }
}
