package cz.cvut.kbss.datatools.bpm2stampo.partners;

import cz.cvut.kbss.datatools.bpm2stampo.partners.csat.ProcessBizagiBPMFile;
import cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.ProcessAdonisExportFile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BPMNConverterRegistry {

    protected static final Map<SupportedBMNFileTypes, AbstractProcessModelExporter> registryMap;
    protected static final Map<String, SupportedBMNFileTypes> extensionMap;
    static{
        Map<SupportedBMNFileTypes, AbstractProcessModelExporter> map = new HashMap<>();
        map.put(SupportedBMNFileTypes.BizagiBPM, new ProcessBizagiBPMFile());
        map.put(SupportedBMNFileTypes.AdonisXML, new ProcessAdonisExportFile());
        registryMap = map;

        extensionMap = Stream.of(SupportedBMNFileTypes.values())
                .collect(Collectors.toMap(st -> st.fileExtension, st -> st));
    }


    public static AbstractProcessModelExporter get(SupportedBMNFileTypes type){
        return registryMap.get(type);
    }

    public static AbstractProcessModelExporter get(String fileName){
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
        return get(extensionMap.get(extension));
    }
}
