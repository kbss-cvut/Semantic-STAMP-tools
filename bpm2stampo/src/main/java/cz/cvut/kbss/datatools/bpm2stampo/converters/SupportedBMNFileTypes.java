package cz.cvut.kbss.datatools.bpm2stampo.converters;

public enum SupportedBMNFileTypes {
    BizagiBPM("bpm"),
    AdonisXML("xml");

    protected String fileExtension;

    SupportedBMNFileTypes(String fileExtension) {
        this.fileExtension = fileExtension;
    }


}
