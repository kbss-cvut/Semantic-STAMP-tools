/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;

public class EccairsQueryUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsQueryUtilsTest.class);

    @Test
    public void testSetAttributeValuesForQueryDataXML() {
        final InputStream is = getClass().getResourceAsStream("/data/webapi/testQueryData.xml");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuffer b = new StringBuffer();
        reader.lines().forEach(s -> b.append(s).append('\n'));

        LOG.info("INPUT: {}",b.toString());

        final EccairsQueryUtils e = new EccairsQueryUtils();

        final String value = "A12345";
        final String result = e.setFileNumberValue(b.toString(), value);
        LOG.info("OUTPUT {}",result);

        Assert.assertTrue(result.contains(value));
    }

    @Test
    public void testSetAttributeValuesForQueryObjectJSON() {
        final InputStream is = getClass().getResourceAsStream("/data/webapi/testQueryObject.json");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuffer b = new StringBuffer();
        reader.lines().forEach(s -> b.append(s).append('\n').append('\r'));

        LOG.info("INPUT: {}",b.toString());

        final EccairsQueryUtils e = new EccairsQueryUtils();

        final String result = e.setAttributeValuesForQueryObjectJSON(b.toString(),new HashMap<String,String>() {{
            put("447","ahoj");
            put("438","nazdar");
        }});

        LOG.info("OUTPUT {}",result);

        final DocumentContext document = JsonPath.parse(result);
        Assert.assertEquals("ahoj", document.<JSONArray>read("$.Restrictions[*][?(@.Attribute.Id == 447)].Values[0].Value").get(0).toString());
        Assert.assertEquals("nazdar", document.<JSONArray>read("$.Restrictions[*][?(@.Attribute.Id == 438)].Values[0].Value").get(0).toString());
    }
}
