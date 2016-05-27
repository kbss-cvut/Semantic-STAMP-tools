'use strict';

import React from "react";
import {Well} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import Actions from "../../actions/Actions";
import ReportStore from "../../stores/ReportStore";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";

export default class UnprocessedReports extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            count: 0
        };
    }

    static UNPROCESSED_PHASE = 'http://onto.fel.cvut.cz/ontologies/aviation-safety/not_processed';

    componentWillMount() {
        this.unsubscribe = ReportStore.listen(this._onReportsLoaded);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onReportsLoaded = (data) => {
        if (data.action !== Actions.loadAllReports) {
            return;
        }
        var unprocessed = 0;
        for (var i = 0, len = data.reports.length; i < len; i++) {
            if (data.reports[i].phase === UnprocessedReports.UNPROCESSED_PHASE) {
                unprocessed++;
            }
        }
        this.setState({count: unprocessed});
    };

    static _goToReports() {
        Routing.transitionTo(Routes.reports, {payload: {filter: {'phase': UnprocessedReports.UNPROCESSED_PHASE}}});
    }

    render() {
        if (this.state.count === 0) {
            return null;
        }
        return <Well>
            <a href='javascript:void(0);' onClick={UnprocessedReports._goToReports} style={{cursor: 'pointer'}}>
                <FormattedMessage id='dashboard.unprocessed'
                                  values={{count: <span className='bold'>{this.state.count}</span>}}/>
            </a>
        </Well>;
    }
}
