/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.eccairs.change;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class EccairsRepositoryChange {

    private Calendar checkDate;
    private final Map<String, EccairsReportChage> changes = new HashMap<>();

    public EccairsRepositoryChange(Calendar date) {
        this.checkDate = date;
    }

    /**
     * 
     * @return The date-time of checking the eccairs serever for changes
     */
    public Calendar getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Calendar checkDate) {
        this.checkDate = checkDate;
    }

    public void addChangedReport(EccairsReportChage change) {
        Objects.requireNonNull(change);
        Objects.requireNonNull(change.getKey());
        Objects.requireNonNull(change.getReportStr());
        if(!change.isCreated() && !change.isEdited()){
            throw new IllegalArgumentException("Malformed change object. Atleast one of the flags chaged, created must be set to true in the change object.");
        }
        changes.put(change.getKey(), change);
    }

    public boolean isEmpty() {
        return changes.isEmpty();
    }

    public EccairsReportChage getReportForEccairsKey(String key) {
        return changes.get(key);
    }

    public int size() {
        return changes.size();
    }

    public Map<String, EccairsReportChage> getChanges() {
        return changes;
    }
}
