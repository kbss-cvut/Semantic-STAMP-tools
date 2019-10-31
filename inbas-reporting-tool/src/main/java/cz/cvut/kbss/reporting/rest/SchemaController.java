package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.exception.AttachmentException;
import cz.cvut.kbss.reporting.exception.SchemaException;
import cz.cvut.kbss.reporting.rest.util.RestUtils;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/schema")
public class SchemaController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaController.class);

    private final ConfigReader configReader;

    @Autowired
    public SchemaController(ConfigReader configReader) {
        this.configReader = configReader;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void importSchema(@RequestParam("file") MultipartFile schema){
        try {
            saveFile(schema.getOriginalFilename(), schema.getInputStream());
        } catch (IOException e) {
            throw new SchemaException("Unable to read file content from request.", e);
        }
//        final HttpHeaders location = RestUtils
//                .createLocationHeaderFromCurrentUri("/{name}", attachment.getOriginalFilename());
//        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    protected void saveFile(String fileName, InputStream content ){
        final File targetDir = getSchemaDir();
        ensureDirectoryExists(targetDir);
        final File schema = new File(targetDir + File.separator + fileName);
        ensureUnique(schema);
        try {
            LOG.info("Writing schema file {}", schema.getAbsolutePath());
            Files.copy(content, schema.toPath());
        } catch (IOException e) {
            throw new AttachmentException("Unable to save attachment to file " + schema.getAbsolutePath(), e);
        } finally {
            try {
                content.close();
            } catch (IOException e) {
                throw new AttachmentException("Unable to close attachment input stream.", e);
            }
        }
    }

    private File getSchemaDir() {
        final String targetDirPath = configReader.getConfig(ConfigParam.SCHEMA_DIR, "");
        if (targetDirPath.isEmpty()) {
            throw new AttachmentException("Schemas directory not configured!");
        }
        final File targetDir = new File(targetDirPath);
        ensureDirectoryExists(targetDir);
        return targetDir;
    }

    private void ensureDirectoryExists(File targetDir) {
        if (!targetDir.exists()) {
            try {
                Files.createDirectories(targetDir.toPath());
            } catch (IOException e) {
                throw new SchemaException("Unable to create Schemas directory " + targetDir.getAbsolutePath(), e);
            }
        }
    }

    private void ensureUnique(File file) {
        if (file.exists()) {
            throw new SchemaException("Schema file " + file.getName() + " already exists.");
        }
    }
}
