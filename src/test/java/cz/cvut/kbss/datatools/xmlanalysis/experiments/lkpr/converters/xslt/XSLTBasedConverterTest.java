package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt;

import org.apache.jena.n3.N3TurtleJenaWriter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class XSLTBasedConverterTest {

    @Ignore
    @Test
    public void convert() {
//        try {
//
//            System.out.println(URLEncoder.encode("asdf+asdf+asdf", "UTF8"));
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        // init and configure the tool

//        XSLTBasedConverter converter = XSLTBasedConverter.createConverterFromCP("/MyStylesheet.xsl");
        XSLTBasedConverter converter = XSLTBasedConverter.createConverterFromCP("/cz/cvut/kbss/datatools/xmlanalysis/lkpr/converters/xslt/adoxml2rdf.xsl");
//        XSLTBasedConverter converter = XSLTBasedConverter.createConverterFromCP("/tmp.xsl");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        converter.convert(
                new File("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\partners\\lkpr-letiste-praha\\Aerobridge.xml"),
                new StreamResult(bos)
//                new StreamResult(System.out)
        );


        String str = bos.toString();
        str = str.replace("&lt;", "<");
        str = str.replace("&gt;", ">");
        System.out.println("line 0");
        System.out.println(str);
        Model m = ModelFactory.createDefaultModel();
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        RDFDataMgr.read(m, bis, "http://base.uri/", Lang.TTL);

//        m.write(System.out, "TTL");
    }

//    @Test
    public void testTmp(){
        String data = "@prefix : <http://base.uri/>.\n" +
                "<CPD+Controller> <attr-Position> [\n" +
                "              :name \"Position\";\n" +
                "              :type \"STRING\";\n" +
                "              :value \"NODE x:4.5cm y:4cm index:1\"\n" +
                "            ];";

        ModelCom m = (ModelCom)ModelFactory.createDefaultModel();
        RDFWriter rdfw = new N3TurtleJenaWriter();

//        m.write()
        ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes());
        RDFDataMgr.read(m, bis, "http://base.uri/", Lang.TTL);
    }

//    @Test
    public void testRDFResourceRendering(){
        Resource r = ResourceFactory.createResource("rdf:type");
        System.out.println(r);
        System.out.println(RDF.type.toString());
        System.out.println(ResourceFactory.createStringLiteral("asdf \"asdf asdf  asdf\""));
        Model m = ModelFactory.createDefaultModel();
        m.add(r, RDF.type, r);

        m.write(System.out, "TTL", "http://base.uri/");
    }
}