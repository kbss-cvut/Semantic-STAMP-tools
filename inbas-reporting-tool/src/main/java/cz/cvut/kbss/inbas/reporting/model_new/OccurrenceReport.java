package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

@OWLClass(iri = Vocabulary.OccurrenceReport)
public class OccurrenceReport implements Serializable {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_documents, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasAuthor, fetch = FetchType.EAGER)
    private Agent author;

    @OWLDataProperty(iri = Vocabulary.p_dateCreated)
    private Date dateCreated;

    @OWLObjectProperty(iri = Vocabulary.p_hasCorrectiveMeasure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasure> correctiveMeasures;
}
