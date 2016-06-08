package cz.cvut.kbss.inbas.reporting.service.formgen;

import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.persistence.dao.formgen.FormGenDao;
import cz.cvut.kbss.inbas.reporting.rest.util.RestUtils;

import java.net.URI;
import java.util.Map;

class EventFormGenDataProcessor extends FormGenDataProcessor<OccurrenceReport> {

    /**
     * Event URI parameter for the form generator.
     */
    static final String EVENT_PARAM = "event";

    EventFormGenDataProcessor(FormGenDao<OccurrenceReport> dao) {
        super(dao);
    }

    @Override
    public void process(OccurrenceReport data, Map<String, String> params) {
        final Integer referenceId = getReferenceId(params);
        super.process(data, params);
        if (referenceId == null) {
            return;
        }

        final SearchByReferenceVisitor visitor = new SearchByReferenceVisitor(referenceId);
        final FactorGraphTraverser traverser = new FactorGraphTraverser(visitor, null);
        traverser.traverse(data.getOccurrence());
        if (visitor.uri == null) {
            throw new IllegalArgumentException(
                    "Event with reference id " + referenceId + " not found in the factor graph.");
        }
        this.params.put(EVENT_PARAM, RestUtils.encodeUrl(visitor.uri.toString()));
    }

    private Integer getReferenceId(Map<String, String> params) {
        if (!params.containsKey(EVENT_PARAM)) {
            return null;
        }
        final Integer referenceId;
        try {
            referenceId = Integer.parseInt(params.get(EVENT_PARAM));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Event reference id " + params.get(EVENT_PARAM) + " is not valid.");
        }
        return referenceId;
    }

    private static final class SearchByReferenceVisitor implements FactorGraphNodeVisitor {

        private final Integer referenceId;
        private URI uri;

        private SearchByReferenceVisitor(Integer referenceId) {
            this.referenceId = referenceId;
        }

        @Override
        public void visit(Occurrence occurrence) {
            if (referenceId.equals(occurrence.getReferenceId())) {
                this.uri = occurrence.getUri();
            }
        }

        @Override
        public void visit(Event event) {
            if (referenceId.equals(event.getReferenceId())) {
                this.uri = event.getUri();
            }
        }
    }
}
