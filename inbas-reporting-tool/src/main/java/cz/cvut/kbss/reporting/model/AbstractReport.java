package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractReport extends AbstractEntity implements LogicalDocument, Serializable {

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

    @OWLObjectProperty(iri = Vocabulary.s_p_references, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected Set<Resource> references;

    @Types
    protected Set<String> types;

    protected AbstractReport() {
        addType(Vocabulary.s_c_report);
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

    public Set<Resource> getReferences() {
        return references;
    }

    public void setReferences(Set<Resource> references) {
        this.references = references;
    }

    /**
     * Adds the specified resource reference to this report.
     *
     * @param resource The resource to add
     */
    public void addReference(Resource resource) {
        Objects.requireNonNull(resource);
        if (references == null) {
            this.references = new HashSet<>(4);
        }
        references.add(resource);
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        Objects.requireNonNull(type);
        if (types == null) {
            this.types = new HashSet<>(4);
        }
        types.add(type);
    }

    protected void copyBasicAttributesToDto(ReportDto dto) {
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
