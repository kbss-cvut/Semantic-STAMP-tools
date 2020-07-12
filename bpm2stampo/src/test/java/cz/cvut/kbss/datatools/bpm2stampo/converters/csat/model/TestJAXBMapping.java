package cz.cvut.kbss.datatools.bpm2stampo.converters.csat.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TestJAXBMapping extends AbstractModelTest{


    @BeforeClass
    public static void setUp() throws IOException {
        AbstractModelTest.setUp();
    }

    @Test
    public void testImplementationSubFLow(){
        bpmProcessor.injectObjectReferences(results);
        List<Object> complexActivities = objects.stream()
                .filter(o -> o instanceof Activity)
                .map(o -> (Activity)o)
                .filter(a -> ((Activity)a).getImplementationSubFlow() != null)
                .collect(Collectors.toList());

        Assert.assertFalse(complexActivities.isEmpty());
        Assert.assertEquals(1, complexActivities.size());
        Activity a = ((Activity)complexActivities.get(0));
        Assert.assertEquals("Task 10 - subprocess", a.getName());
        Assert.assertNotNull(a.getImplementationSubFlow());
        Assert.assertEquals("b6222bab-e366-4b5f-a723-fc983f01c81f", a.getImplementationSubFlow().getReferencedProcessId());
    }

}
