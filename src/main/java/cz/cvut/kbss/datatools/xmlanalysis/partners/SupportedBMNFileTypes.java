package cz.cvut.kbss.datatools.xmlanalysis.partners;

public enum SupportedBMNFileTypes {
    BizagiBPM("bpm"),
    AdonisXML("xml");

    protected String fileExtension;

    SupportedBMNFileTypes(String fileExtension) {
        this.fileExtension = fileExtension;
    }


}
