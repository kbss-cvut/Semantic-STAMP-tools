package cz.cvut.kbss.eccairs.webapi;

import cz.cvut.kbss.eccairs.webapi.cvut.EwaIWebServer;
import cz.cvut.kbss.eccairs.webapi.cvut.EwaResult;
import cz.cvut.kbss.eccairs.webapi.cvut.EwaWebServer;
import cz.cvut.kbss.utils.SslUtilities;

/**
 * Created by kremep1 on 17/02/16.
 */
public class TestClient {

  static {
    SslUtilities.trustAllHostnames();
    SslUtilities.trustAllHttpsCertificates();
  }

  /**
   * Prints basic info about the repository. Tries to fetch a few records.
   *
   * @param argv No used.
   */
  public static void main(String[] argv) {
    EwaIWebServer service = new EwaWebServer().getBasicHttpBindingEwaIWebServer();

    // get data
    System.out.println(service.getRepositories().getData().getValue());

    // login
    EwaResult login = service.login("MSSQL REPOSITORY 3.4.0.2 CUSTOM", "webtest", "xxx", "en");

    String token = login.getUserToken().getValue();
    System.out.println(token + " : " + login.getReturnCode());

    EwaResult result = service.executeQueryAsTable(token, "CTU Library", "ALL", 0);
    System.out.println(result.getData().getValue());

    EwaResult resultO = service.getOccurrence(token, "000035/2015", 2046);
    System.out.println(resultO.getData().getValue());

    EwaResult resultOd = service.getOccurrenceData(token, "000035/2015", 2046);
    System.out.println(resultOd.getData().getValue());

    resultO = service.getOccurrence(token, "CZ-07-305", 1046);
    System.out.println(resultO.getData().getValue());

    resultOd = service.getOccurrenceData(token, "CZ-07-305", 1046);
    System.out.println(resultOd.getData().getValue());
  }
}
