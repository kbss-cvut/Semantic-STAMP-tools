package cz.cvut.kbss.eccairs.serviceapi;

import cz.cvut.kbss.eccairs.serviceapi.uzpln.ErscISrvConnector;
import cz.cvut.kbss.eccairs.serviceapi.uzpln.ErssSrvConnector;
import cz.cvut.kbss.utils.SslUtilities;

/**
 * Test client for Eccairs Service.
 */
final class TestClient {

  static {
    SslUtilities.trustAllHostnames();
    SslUtilities.trustAllHttpsCertificates();
  }

  /**
   * Main test method.
   *
   * @param argv not used
   */
  public static void main(final String[] argv) {
    ErscISrvConnector service =
        new ErssSrvConnector().getBasicHttpBindingErscISrvConnector();

    System.out.println(service.configurationErrorCode());

    // get data
    System.out.println(service.getIsAlive());
  }
}
