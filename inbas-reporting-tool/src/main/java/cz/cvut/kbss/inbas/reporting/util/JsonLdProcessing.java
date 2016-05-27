package cz.cvut.kbss.inbas.reporting.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.inbas.reporting.exception.JsonProcessingException;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;

import java.io.IOException;
import java.net.URI;
import java.util.*;

public class JsonLdProcessing {

    private static final String ID_PROPERTY = "@id";

    private JsonLdProcessing() {
        throw new AssertionError();
    }

    public static List<URI> getOrderedOptions(RawJson json, String greaterThanProperty) {
        Objects.requireNonNull(json);
        Objects.requireNonNull(greaterThanProperty);
        try {
            return readAndOrderElements(json, greaterThanProperty);
        } catch (IOException e) {
            throw new JsonProcessingException("The specified JSON is not valid. JSON: " + json, e);
        }
    }

    private static List<URI> readAndOrderElements(RawJson json, String greaterThanProperty) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<URI[]> lst = new ArrayList<>();
        final JsonNode root = objectMapper.readTree(json.getValue());
        if (root == null) {
            return Collections.emptyList();
        }
        Iterator<JsonNode> elements = root.elements();
        while (elements.hasNext()) {
            final JsonNode n = elements.next();
            final URI id = URI.create(n.path(ID_PROPERTY).asText());
            final JsonNode gt = n.path(greaterThanProperty);
            URI gtNode = null;
            if (!gt.isMissingNode()) {
                gtNode = URI.create(gt.elements().next().path(ID_PROPERTY).asText());
            }
            lst.add(new URI[]{id, gtNode});
        }
        return sortElements(lst);
    }

    private static List<URI> sortElements(List<URI[]> elements) {
        final List<URI> res = new ArrayList<>();
        int runs = 0;
        while (res.size() < elements.size()) {
            if (runs > elements.size()) {
                throw new JsonProcessingException("The specified JSON does not contain options that can be sorted.");
            }
            for (URI[] el : elements) {
                // Either the result is empty and we are looking for the lowest one
                // Or we take the one whose predecessor is currently last in the result list
                if ((res.isEmpty() && el[1] == null) || (!res.isEmpty() && res.get(res.size() - 1).equals(el[1]))) {
                    res.add(el[0]);
                    break;
                }
            }
            runs++;
        }
        return res;
    }
}
