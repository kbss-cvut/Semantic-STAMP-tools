package cz.cvut.inbas.eccairs.test;

import cz.cvut.inbas.eccairs.model.EwaIWebServer;
import cz.cvut.inbas.eccairs.model.EwaResult;
import cz.cvut.inbas.eccairs.model.EwaWebServer;

/**
 * Created by kremep1 on 17/02/16.
 */
public class TestClient {

  static {
    SSLUtilities.trustAllHostnames();
    SSLUtilities.trustAllHttpsCertificates();
  }

  public static void main(String[] argv) {
    EwaIWebServer service = new EwaWebServer().getBasicHttpBindingEwaIWebServer();

    // get data
    System.out.println(service.getRepositories().getData().getValue());

    // login
    EwaResult login=service.login("MSSQL REPOSITORY 3.4.0.1", "webtest", "ilovešýž", "en");

    String token = login.getUserToken().getValue();
    System.out.println(token + " : " + login.getReturnCode());

    EwaResult result = service.executeQueryAsTable(token, "CTU Library", "ALL", 0);
    System.out.println(result.getData().getValue());

    EwaResult resultO = service.getOccurrence(token, "000035/2015", 2046);
    System.out.println(resultO.getData().getValue());

    EwaResult resultOD = service.getOccurrenceData(token, "000035/2015", 2046);
    System.out.println(resultOD.getData().getValue());
    
    resultO = service.getOccurrence(token, "CZ-07-305", 1046);
    System.out.println(resultO.getData().getValue());
    
    resultOD = service.getOccurrenceData(token, "CZ-07-305", 1046);
    System.out.println(resultOD.getData().getValue());
  }
}
