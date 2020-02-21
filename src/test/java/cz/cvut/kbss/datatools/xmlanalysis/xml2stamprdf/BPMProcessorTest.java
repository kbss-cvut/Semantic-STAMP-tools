package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Activity;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Event;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Package;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

public class BPMProcessorTest {
    @Test
    public void testUnmarshalBizzagi(){
        BPMProcessor p = new BPMProcessor();
        URL url = this.getClass().getClassLoader().getResource("./bizagi/example-model-1/db0d3329-5361-4a9f-8a1b-e834e22e288c/Diagram.xml");
        Assert.assertNotNull(url);

        try(InputStream is = url.openStream()) {
            InputXmlStream f = new InputXmlStream("Diagram.xml", is, Package.class);
            JAXBUtils.UnmarshledResult r = p.unmarshal(f);
            Set<Object> os = r.getObjects();

            // a start event example
            Activity sea = (Activity)os.stream()
                    .filter(o -> o instanceof Activity)
                    .filter(o -> ((Activity)o).getId().equals("72d51bbe-b8d0-4a40-9d7f-2fe6242040e7"))
                    .findAny().orElse(null);
            Assert.assertEquals("", sea.getName());
            Assert.assertTrue(sea.isStartEvent());
            Assert.assertFalse(sea.isIntermediateEvent());
            Assert.assertFalse(sea.isEndEvent());
            Assert.assertNotNull("", sea.getStartEvent());
            Assert.assertNotNull("Message", sea.getStartEvent().getTrigger());
            Assert.assertFalse(sea.getStartEvent().isAttached());
            Assert.assertNull(sea.getStartEvent().getTarget());

            //AError 1 - an IntermediateEvent example
            sea = (Activity)os.stream()
                    .filter(o -> o instanceof Activity)
                    .filter(o -> ((Activity)o).getId().equals("abdd6123-678c-426a-a709-0d2f58fbfb79"))
                    .findAny().orElse(null);
            Assert.assertEquals("AError 1", sea.getName());
            Assert.assertTrue(sea.isIntermediateEvent());
            Assert.assertFalse(sea.isStartEvent());
            Assert.assertFalse(sea.isEndEvent());
            Assert.assertNotNull("", sea.getIntermediateEvent());

            Event e = sea.getIntermediateEvent();
            Assert.assertNotNull("Message", e.getTrigger());
            Assert.assertTrue(e.isAttached());
            Assert.assertEquals("4832f7c3-37ee-435e-876e-42ead2090683", e.getTarget());

            //5ee49508-dfd0-45a1-82ec-fc604f3bd6e5 - "Task 3 - Send Message 1" - example Task
            sea = (Activity)os.stream()
                    .filter(o -> o instanceof Activity)
                    .filter(o -> ((Activity)o).getId().equals("5ee49508-dfd0-45a1-82ec-fc604f3bd6e5"))
                    .findAny().orElse(null);

            Assert.assertEquals("Task 3 - Send Message 1", sea.getName());
            Assert.assertTrue(sea.isImplementation());
            Assert.assertTrue(sea.isImplementationTask());
            Assert.assertFalse(sea.isImplementationSubFlow());


            //4d118be7-4485-4b52-a435-0cb1680ca4fb - "Task 10 - subprocess" - example SubFlow
            sea = (Activity)os.stream()
                    .filter(o -> o instanceof Activity)
                    .filter(o -> ((Activity)o).getId().equals("4d118be7-4485-4b52-a435-0cb1680ca4fb"))
                    .findAny().orElse(null);

            Assert.assertEquals("Task 10 - subprocess", sea.getName());
            Assert.assertTrue(sea.isImplementation());
            Assert.assertFalse(sea.isImplementationTask());
            Assert.assertTrue(sea.isImplementationSubFlow());
            Assert.assertEquals("b6222bab-e366-4b5f-a723-fc983f01c81f", sea.getImplementationSubFlow().getReferencedProcessId());



        }catch (IOException e){
            e.printStackTrace();
        }
    }


}