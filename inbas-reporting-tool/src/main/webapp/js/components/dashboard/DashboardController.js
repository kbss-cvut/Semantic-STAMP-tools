'use strict';

import React from "react";
import Actions from "../../actions/Actions";
import Dashboard from "./Dashboard";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";
import UserStore from "../../stores/UserStore";

export default class DashboardController extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            firstName: UserStore.getCurrentUser() ? UserStore.getCurrentUser().firstName : ''
        };
    }

    componentDidMount() {
        Actions.loadAllReports();
        this.unsubscribe = UserStore.listen(this._onUserLoaded);
    }

    _onUserLoaded = (user) => {
        this.setState({firstName: user.firstName});
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    createEmptyReport = (reportType) => {
        Routing.transitionTo(Routes.createReport, {
            handlers: {
                onSuccess: Routes.reports,
                onCancel: Routes.dashboard
            },
            payload: {
                reportType: reportType
            }
        });
    };

    importE5Report = (file, onFinish, onError) => {
        Actions.importE5Report(file, (key) => {
            onFinish();
            Routing.transitionTo(Routes.editReport, {
                params: {reportKey: key},
                handlers: {onCancel: Routes.dashboard}
            });
        }, onError);
    };

    importSafaReports = (file, onFinish, onError) => {
        Actions.importSafaExcel(file, () => {
            onFinish();
            this.showReports();
        }, onError);
    };

    openReport = (report) => {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: report.key},
            handlers: {onCancel: Routes.reports}
        });
    };

    showReports = () => {
        Routing.transitionTo(Routes.reports);
    };


    render() {
        const importHandlers = {
            importE5: this.importE5Report,
            importSafa: this.importSafaReports
        };
        return <div>
            <Dashboard userFirstName={this.state.firstName}
                       showAllReports={this.showReports} createEmptyReport={this.createEmptyReport}
                       importHandlers={importHandlers} openReport={this.openReport}/>
        </div>;
    }
}
