package cz.cvut.kbss.datatools.bpm2stampo.partners.csat.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TestObjectInjection extends AbstractModelTest{

    @BeforeClass
    public static void setUp() throws IOException {
        AbstractModelTest.setUp();
        bpmProcessor.injectObjectReferences(results);
    }

    @Test
    public void testWorkFlowProcessInjectedInActivity(){
        List<Object> complexActivities = objects.stream()
                .filter(o -> o instanceof Activity)
                .map(o -> (Activity)o)
                .filter(a -> a.getImplementationSubFlow() != null)
                .collect(Collectors.toList());

        Activity a = ((Activity)complexActivities.get(0));
        WorkflowProcess wp = a.getSubProcess();
        Assert.assertNotNull(wp);
        Assert.assertEquals("Main Process", wp.getName());
        Assert.assertEquals("b6222bab-e366-4b5f-a723-fc983f01c81f", wp.getId());
//        Assert.assertEquals("", wp.get);
    }

}
