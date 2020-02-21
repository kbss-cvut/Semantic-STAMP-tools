package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb;

import cz.cvut.kbss.adoxml.ADOXML;
import cz.cvut.kbss.adoxml.MODEL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class LoadModelJAXB {

    protected ADOXML adoxml;

    public void process(File f){
        load(f);
        process(adoxml);
    }



    public void load(File file){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ADOXML.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            adoxml = (ADOXML) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    protected  void process(ADOXML adoxml) {
        List<MODEL> list = adoxml.getMODELS().getMODEL();
        System.out.println(list.size());
        if(!list.isEmpty()){
            System.out.println(list.get(0).getName() + " - " + list.get(0).getId());
        }
    }





    public static void main(String[] args) {
        File file = new File("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\lkpr-process-models\\example-models-01\\Aerobridge.xml");
        LoadModelJAXB processor = new LoadModelJAXB();
        processor.process(file);
    }
}
