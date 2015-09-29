'use strict';

/**
 * Jasmine configuration.
 */

jasmine.VERBOSE = true;

require('jasmine-reporters');
var reporter = new jasmine.JUnitXmlReporter("../../../target/surefire-reports/");
jasmine.getEnv().addReporter(reporter);
