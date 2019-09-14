package cz.cvut.kbss.datatools.xmlanalysis.experiments.xpathtransforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import java.util.Optional;

public class TFunction {

    private static final Logger LOG = LoggerFactory.getLogger(TFunction.class);

    protected String key;
    protected XPathExpression xp;
    protected String defaultValue;
    protected QName type = XPathConstants.STRING;


    public TFunction(XPathExpression xp, String defaultValue) {


        this.xp = xp;
        this.defaultValue = defaultValue;
    }

    public Optional<String> getOpt(Node from){
        try {
            return Optional.ofNullable((String)xp.evaluate(from, type))
                    .map(a -> a.trim());
        }catch (XPathException e){
            LOG.error("", e);
        }
        return Optional.ofNullable(null);
    }


    public String get(Node from){
        return getOpt(from).orElse(defaultValue);
    }

}
