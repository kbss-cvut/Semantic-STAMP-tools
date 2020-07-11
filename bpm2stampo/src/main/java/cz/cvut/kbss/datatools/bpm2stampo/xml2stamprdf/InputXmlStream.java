package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf;

import cz.cvut.kbss.commons.io.NamedStream;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class InputXmlStream<T> extends NamedStream {

    protected Class<T> root;
    protected String parentDir;

    public InputXmlStream(String name, InputStream is) {
        super(name, is);
    }

    public InputXmlStream(String filePath) throws FileNotFoundException {
        super(filePath);
    }

    public InputXmlStream(String name, InputStream is, Class root) {
        super(name, is);
        this.root = root;
    }

    public InputXmlStream(String name, String parentDir, InputStream is, Class<T> root) {
        super(name, is);
        this.root = root;
        this.parentDir = parentDir;
    }

    public InputXmlStream(String filePath, Class<T> root) throws FileNotFoundException {
        super(filePath);
        this.root = root;
    }

    public Class<T> getRoot() {
        return root;
    }

    public void setRoot(Class<T> root) {
        this.root = root;
    }

    public String getParentDir() {
        return parentDir;
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }
}
