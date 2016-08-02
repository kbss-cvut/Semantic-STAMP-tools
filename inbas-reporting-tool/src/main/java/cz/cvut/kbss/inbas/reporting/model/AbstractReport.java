package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractReport implements LogicalDocument {

    @Id(generated = true)
    URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_key)
    String key;

    /**
     * File number identifies a particular report chain, i.e. revisions of the same report.
     */
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_file_number)
    Long fileNumber;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_has_author, fetch = FetchType.EAGER)
    Person author;

    @OWLDataProperty(iri = Vocabulary.s_p_created)
    Date dateCreated;

    @OWLDataProperty(iri = Vocabulary.s_p_modified)
    Date lastModified;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_last_editor, fetch = FetchType.EAGER)
    Person lastModifiedBy;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_revision)
    Integer revision;

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    String summary;

    @Types
    Set<String> types;

    protected AbstractReport() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_report);
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Long getFileNumber() {
        return fileNumber;
    }

    @Override
    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    @Override
    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Person getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Person lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    protected void copyAttributes(ReportDto dto) {
        dto.setUri(uri);
        dto.setKey(key);
        dto.setFileNumber(fileNumber);
        dto.setAuthor(author);
        dto.setDateCreated(dateCreated);
        dto.setLastModifiedBy(lastModifiedBy);
        dto.setLastModified(lastModified);
        dto.setRevision(revision);
        dto.setTypes(types != null ? new HashSet<>(types) : new HashSet<>());
    }
}
