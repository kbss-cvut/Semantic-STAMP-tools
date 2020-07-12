package cz.cvut.kbss.datatools.bpm2stampo.converters;

import cz.cvut.kbss.datatools.bpm2stampo.converters.csat.ProcessBizagiBPMFile;
import cz.cvut.kbss.datatools.bpm2stampo.converters.lkpr.ProcessAdonisExportFile;
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