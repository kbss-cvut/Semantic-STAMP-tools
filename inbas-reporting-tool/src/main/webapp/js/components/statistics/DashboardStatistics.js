'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import Actions from "../../actions/Actions";
import Constants from "../../constants/Constants";
import OptionsStore from "../../stores/OptionsStore";

export default class DashboardStatistics extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onConfigLoaded);
        Actions.loadStatisticsConfig();
    }

    _onConfigLoaded = (data) => {
        if (data.action === Actions.loadStatisticsConfig) {
            this.forceUpdate();
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    render() {
        const src = OptionsStore.getStatisticsConfiguration()[Constants.STATISTICS_DASHBOARD];
        return <Panel bsStyle='primary'>
            <iframe className='dashboard-statistics-frame' src={src} allowFullScreen='false'></iframe>
        </Panel>;
    }
};
