package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EccairsService {

    /**
     * Gets the latest occurrence report from the ECCAIRS report repository matching the report with key
     *
     * @param key Report key
     * @return Matching report
     */
    OccurrenceReport getEccairsLatestByKey(String key);

    OccurrenceReport getEccairsLatestByJson(String rdfJsonLd);

    URI findAndLoadLatestEccairsReport(String reportingEntity, String reportingEntityFileNumber);

    /**
     * Returns an ECCAIRS occurrence that is backed an initial report with the given identification.
     *
     * @return an E5F serialization of the report
     */
    String getCurrentEccairsReportByInitialFileNumberAndReportingEntity(String reportingEntity, String reportNumber);

    /**
     * Returns all occurrences (AAII occurrence reports integrating possibly multiple records of occurrences) from ECCAIRS
     *
     * @return a list of E5F serializations in strings
     */
    List<String> getAllOccurrences();

    /**
     * Returns an ECCAIRS occurrence that is backed an initial report with the given identification.
     * <p>
     * Example: final Calendar cal = Calendar.getInstance();
     * cal.set(2015,12,01);
     * getOccurrencesAfterCreationDate(cal);
     *
     * @param minimumCreationDate
     * @return an E5F serialization of the reports
     */
    List<String> getOccurrencesAfterCreationDate(Calendar minimumCreationDate);

    /**
     * Returns an ECCAIRS occurrence that is backed an initial report with the given identification.
     * <p>
     * Example: final Calendar cal = Calendar.getInstance();
     * cal.set(2016,02,01);
     * getOccurrencesAfterModifiedDate(cal);
     *
     * @param minimumModifiedDate
     * @return an E5F serialization of the reports
     */
    List<String> getOccurrencesAfterModifiedDate(Calendar minimumModifiedDate);
    
    /**
     * Gets the latest changes from the eccairs repository. The chages are returned 
     * in a EccairsRepositoryChange object. The returned object 
     * contains the newly created reports and the reports that were changed. If
     * a report is found to be botht created and updated after the given date
     * the updated report is taken but it is still classified as a create one.
     * 
     * @param date
     * @return 
     */
    public EccairsRepositoryChange getLatestChanges(final Calendar date);
    
    /**
     * This class represent the changes made in the repository from a specific date.
     * 
     */
    public static class EccairsRepositoryChange {

        private Calendar date;
        private Map<String, String> newReports = new HashMap<>();
        private Map<String, String> changedReports = new HashMap<>();

        public EccairsRepositoryChange(Calendar date) {
            this.date = date;
        }

        public Calendar getDate() {
            return date;
        }

        public void setDate(Calendar date) {
            this.date = date;
        }

        public Map<String, String> getNewReports() {
            return newReports;
        }

        public void setNewReports(Map<String, String> newReports) {
            this.newReports = newReports;
        }

        public Map<String, String> getChangedReports() {
            return changedReports;
        }

        public void setChangedReports(Map<String, String> changedReports) {
            this.changedReports = changedReports;
        }
    }
}
