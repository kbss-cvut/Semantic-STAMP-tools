package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Properties;

public class XSLTBasedConverter {

    private static final Logger LOG = LoggerFactory.getLogger(XSLTBasedConverter.class);

    public static final String STYLESHEET_CP = "STYLESHEET_CP";
    public static final String STYLESHEET_PATH = "STYLESHEET_PATH";

    protected Properties config;
    protected TransformerFactory transformerFactory;
    protected Transformer transformer;

    public XSLTBasedConverter() {

    }

    public XSLTBasedConverter(Properties config) {
        init(config);
    }

    /******************************************************************************************************************/
    /**** Init code ***************************************************************************************************/
    /******************************************************************************************************************/
    protected void init(Properties config){
        this.config = config;
        transformerFactory = createTransformerFactory();
        String stylesheetPath = config.getProperty(STYLESHEET_CP);
        if(stylesheetPath != null) {
            transformer = createTransformerFromCP(stylesheetPath);
        }else{
            stylesheetPath = config.getProperty(STYLESHEET_PATH);
            transformer = createTransformerFromFS(stylesheetPath);
        }
    }

    protected TransformerFactory createTransformerFactory(){
        return TransformerFactory.newInstance();
    }

    protected Transformer createTransformerFromCP(String cp) {
        InputStream is = XSLTBasedConverter.class.getResourceAsStream(cp);
        return createTransformer(is);
    }

    protected Transformer createTransformerFromFS(String fsPath){
        try {
            InputStream is = new FileInputStream(fsPath);
            return createTransformer(is);
        } catch (FileNotFoundException e) {
            LOG.error("", e);
        }
        return null;
    }

    protected Transformer createTransformer(InputStream styleSheet){
        StreamSource streamSource = new StreamSource(styleSheet);
        try {
            return transformerFactory.newTransformer(streamSource);
        } catch (TransformerConfigurationException e) {
            LOG.error("", e);
        }
        return null;
    }

    public Properties getConfig() {
        return config;
    }

    /******************************************************************************************************************/
    /**** Functional Code *********************************************************************************************/
    /******************************************************************************************************************/

    public void convert(File inputFile, StreamResult streamResult) {
        try {
//            final Schema s = Configuration.getConfiguredSchema();
            streamResult.getOutputStream().write("@prefix : <http://base.uri/>.\n".getBytes());
            StreamSource toBeTransformed = new StreamSource(inputFile);
            transformer.transform(toBeTransformed, streamResult);
        } catch (TransformerException | IOException e ) {
            LOG.error("", e);
        }
    }

    /******************************************************************************************************************/
    /**** Factory methods *********************************************************************************************/
    /******************************************************************************************************************/

    public static XSLTBasedConverter createConverterFromCP(String stylesheetOnCp){
        return createConverterImpl(STYLESHEET_CP, stylesheetOnCp);
    }

    public static XSLTBasedConverter createConverterFromFS(String stylesheetOnFS){
        return createConverterImpl(STYLESHEET_PATH, stylesheetOnFS);
    }


    public static XSLTBasedConverter createConverterImpl(String propertyName, String val){
        Properties config = new Properties();
        config.setProperty(propertyName, val);
        XSLTBasedConverter converter = new XSLTBasedConverter(config);
        return converter;
    }
}
