package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractReport extends AbstractEntity implements LogicalDocument {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_key)
    protected String key;

    /**
     * File number identifies a particular report chain, i.e. revisions of the same report.
     */
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_file_number)
    protected Long fileNumber;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_has_author, fetch = FetchType.EAGER)
    protected Person author;

    @OWLDataProperty(iri = Vocabulary.s_p_created)
    protected Date dateCreated;

    @OWLDataProperty(iri = Vocabulary.s_p_modified)
    protected Date lastModified;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_last_editor, fetch = FetchType.EAGER)
    protected Person lastModifiedBy;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_revision)
    protected Integer revision;

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    protected String summary;

    @Types
    protected Set<String> types;

    protected AbstractReport() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_report);
    }

    protected AbstractReport(AbstractReport other) {
        Objects.requireNonNull(other);
        this.fileNumber = other.fileNumber;
        this.summary = other.summary;
        this.types = new HashSet<>(other.types);
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
