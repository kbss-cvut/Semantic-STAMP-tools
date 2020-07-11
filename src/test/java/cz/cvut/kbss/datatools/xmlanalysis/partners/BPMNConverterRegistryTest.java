package cz.cvut.kbss.datatools.xmlanalysis.partners;

import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.ProcessBizagiBPMFile;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.ProcessAdonisExportFile;
import junit.framework.TestCase;

public class BPMNConverterRegistryTest extends TestCase {

    public void testGetString() {
        AbstractProcessModelExporter processor = BPMNConverterRegistry.get("file.bpm");
        assertNotNull(processor);
        assertEquals(ProcessBizagiBPMFile.class, processor.getClass());

        processor = BPMNConverterRegistry.get("file.xml");
        assertNotNull(processor);
        assertEquals(ProcessAdonisExportFile.class, processor.getClass());
    }

}