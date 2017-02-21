'use strict';

import React from "react";
import {Well} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import CreateReportDashboard from "./CreateReportDashboard";
import DashboardStatistics from "../statistics/DashboardStatistics";
import I18nWrapper from "../../i18n/I18nWrapper";
import ImportReport from "./ImportReport";
import injectIntl from "../../utils/injectIntl";
import RecentlyEdited from "./RecentlyEditedReports";
import UnprocessedReports from "./UnprocessedReports";

class Dashboard extends React.Component {

    static propTypes = {
        createEmptyReport: React.PropTypes.func.isRequired,
        importHandlers: React.PropTypes.object.isRequired,
        showAllReports: React.PropTypes.func.isRequired,
        openReport: React.PropTypes.func.isRequired,
        userFirstName: React.PropTypes.string,
        statistics: React.PropTypes.func
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showImport: false
        };
    }

    onUserLoaded = (user) => {
        this.setState({firstName: user.firstName});
    };

    importReport = () => {
        this.setState({showImport: true});
    };

    cancelImport = () => {
        this.setState({showImport: false});
    };


    render() {
        return <div style={{margin: '0 -15px 0 -15px'}}>
            <ImportReport importHandlers={this.props.importHandlers} onClose={this.cancelImport}
                          show={this.state.showImport}/>
            <div className='dashboard-left'>
                {this._renderDashboard()}
                <DashboardStatistics/>
            </div>
            <div className='dashboard-right'>
                <div>
                    <RecentlyEdited reports={this.props.reports} onOpenReport={this.props.openReport}/>
                </div>
                <div>
                    <UnprocessedReports />
                </div>
            </div>
        </div>;
    }

    _renderDashboard() {
        return <Well>
            <h3 className='dashboard-welcome'>
                <FormattedMessage id='dashboard.welcome'
                                  values={{name: <span className='bold'>{this.props.userFirstName}</span>}}/>
            </h3>
            <CreateReportDashboard createReport={this.props.createEmptyReport} importReport={this.importReport}/>
        </Well>;
    }
}

export default injectIntl(I18nWrapper(Dashboard));
