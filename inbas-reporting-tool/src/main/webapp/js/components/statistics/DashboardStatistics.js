'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import Constants from "../../constants/Constants";

const DashboardStatistics = () => {
    return <Panel bsStyle='primary'>
        <iframe className='dashboard-statistics-frame' src={Constants.DASHBOARD_STATISTICS_URL}
                allowFullScreen='false'></iframe>
    </Panel>;
};

export default DashboardStatistics;
