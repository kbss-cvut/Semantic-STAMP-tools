'use strict';

var Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'INBAS Reporting Tool',
    CONFLICTING_AIRCRAFT_STEP_ID: 'conflictingAircraft',
    ECCAIRS_URL: 'http://www.icao.int/safety/airnavigation/AIG/Documents/ADREP%20Taxonomy/ECCAIRS%20Aviation%201.3.0.12%20(Entities%20and%20Attributes).en.id.pdf',
    HOME_ROUTE: Routes.dashboard,
    PRELIMINARY_REPORT_PHASE: 'PRELIMINARY',
    INVESTIGATION_REPORT_PHASE: 'INVESTIGATION',
    LINK_TYPES: {
        CAUSE: 'cause',
        MITIGATE: 'mitigate'
    },

    PRELIMINARY_REPORT_TYPE: 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#PreliminaryReport',
    INVESTIGATION_REPORT_TYPE: 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#InvestigationReport',

    UNAUTHORIZED_USER: {name: 'unauthorized'}
};
