package cz.cvut.kbss.reporting.service.data.export;

import cz.cvut.kbss.reporting.data.eccairs.Aso2E5X;
import cz.cvut.kbss.reporting.data.eccairs.E5XTerms;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.service.ReportBusinessService;
import cz.cvut.kbss.reporting.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import java.io.*;
import java.net.MalformedURLException;

/**
 * Created by user on 5/24/2017.
 */
@Service
public class ReportExporter {
    private static final Logger LOG = LoggerFactory.getLogger(ReportExporter.class);

    @Autowired
    protected ReportBusinessService reportService;

    protected Schema e5xSchema;

    @PostConstruct
    protected void init(){
        try {
            e5xSchema = Aso2E5X.loadSchema(E5XTerms.dataBridgeNS);
        } catch (MalformedURLException e) {
            LOG.error(String.format("could not load e5x schema, malformed URL \"%s\"", E5XTerms.dataBridgeNS), e);
            LOG.warn("generated e5x failes will not be validated.");
        }
    }

    public byte[] exportReportToE5X(String key, boolean zip){
        LOG.info("read report with key {}", key);
        OccurrenceReport report = (OccurrenceReport)reportService.findByKey(key);
        String fileNumber = Long.toString(report.getFileNumber());
        String reportRevision = fileNumber + ":" + key;
        Aso2E5X aso2E5XTExporter = new Aso2E5X(e5xSchema);
        try {
            LOG.info("converting report with key {} to e5x DOM", key);
            Document doc = aso2E5XTExporter.convert(report);
            LOG.info("serializing report with key {} to e5x xml", key);
            ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
            Aso2E5X.serializeDocument(doc, reportStream);

            LOG.info("validate the e5x xml output");
            Aso2E5X.validateDocument(reportRevision,doc);

            // for debugging
            try {
                String output = reportStream.toString(Constants.UTF_8_ENCODING);
                System.out.println(output);
            } catch (UnsupportedEncodingException e) {
                LOG.error(String.format("The encoding %s used to encode/decode the e5x output is not supported", Constants.UTF_8_ENCODING), e);
            }

            byte[] outputBytes = reportStream.toByteArray();

            if(zip){
                LOG.info("zipping e5x xml to e5x file");
                ByteArrayOutputStream e5xZipped = new ByteArrayOutputStream();
                Aso2E5X.generateE5XFile(outputBytes, e5xZipped, reportRevision);
                outputBytes = e5xZipped.toByteArray();
            }

            return outputBytes;

        } catch (ParserConfigurationException e) {
            LOG.error(String.format("cannot convert occurrence report with key=\"%s\"to e5x xml, failed to create document builder with given configuration ", key), e);
        } catch (MalformedURLException e) {
            LOG.error(String.format("cannot convert occurrence report with key=\"%s\"to e5x xml, bad schema url=\"%s\"", key, E5XTerms.dataBridgeNS), e);
        } catch (IOException e) {
            LOG.error(String.format("cannot convert occurrence report with key=\"%s\"to e5x xml, io error while zipping e5x xml content.", key, E5XTerms.dataBridgeNS), e);
        }
        return null;
    }
}
