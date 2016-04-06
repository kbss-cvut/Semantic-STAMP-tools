package cz.cvut.kbss.inbas.reporting.dto.util;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

// Note that every reference has to be defined before its first use
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "referenceId")
public abstract class HasReferenceId {

    // Reference ID for JSON references
    protected Integer referenceId;

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}
