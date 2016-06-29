package cz.cvut.kbss.eccairs.serviceapi;

import cz.cvut.kbss.eccairs.serviceapi.uzpln.ErscISrvConnector;
import cz.cvut.kbss.eccairs.serviceapi.uzpln.ErssSrvConnector;
import cz.cvut.kbss.utils.SSLUtilities;

public class TestClient {

  static {
    SSLUtilities.trustAllHostnames();
    SSLUtilities.trustAllHttpsCertificates();
  }

  public static void main(String[] argv) {
    ErscISrvConnector service = new ErssSrvConnector().getBasicHttpBindingErscISrvConnector();

    System.out.println(service.configurationErrorCode());

    // get data
    System.out.println(service.getIsAlive());
  }
}
