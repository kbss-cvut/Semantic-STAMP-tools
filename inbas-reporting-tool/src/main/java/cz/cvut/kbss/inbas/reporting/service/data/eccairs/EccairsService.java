package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

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
     * @return an E5F serialization of the report
     */
    String getOccurrencesAfterCreationDate(Calendar minimumCreationDate);

    /**
     * Returns an ECCAIRS occurrence that is backed an initial report with the given identification.
     * <p>
     * Example: final Calendar cal = Calendar.getInstance();
     * cal.set(2016,02,01);
     * getOccurrencesAfterModifiedDate(cal);
     *
     * @return an E5F serialization of the report
     */
    String getOccurrencesAfterModifiedDate(Calendar minimumModifiedDate);
}
