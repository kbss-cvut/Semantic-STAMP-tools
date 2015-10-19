package cz.cvut.kbss.inbas.audit.services.options;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class FileOptionsLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FileOptionsLoader.class);

    public String load(String fileName) {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IllegalArgumentException("No options file found with name " + fileName);
        }
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(is))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            LOG.error("Unable to load options from file.", e);
            return "";
        }
    }
}
