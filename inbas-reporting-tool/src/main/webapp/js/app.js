/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var React = require('react');
var ReactDOM = require('react-dom');
var Router = require('react-router').Router;
var Route = require('react-router').Route;
var IndexRoute = require('react-router').IndexRoute;

var history = require('./utils/Routing').history;
var Routes = require('./utils/Routes');
var Actions = require('./actions/Actions');

var Login = require('./components/login/Login');
var Register = require('./components/register/Register');
var MainView = require('./components/MainView');
var DashboardController = require('./components/dashboard/DashboardController');
var ReportsController = require('./components/reports/ReportsController');
var PreliminaryReportsController = require('./components/preliminary/ReportsController');
var PreliminaryReportController = require('./components/preliminary/ReportDetailController');
var InvestigationsController = require('./components/investigation/InvestigationsController');
var InvestigationController = require('./components/investigation/InvestigationController');


Actions.loadUser();

ReactDOM.render((
    <Router history={history}>
        <Route path='/' component={MainView}>
            <IndexRoute component={DashboardController}/>
            <Route path={Routes.login.path} component={Login}/>
            <Route path={Routes.register.path} component={Register}/>
            <Route path={Routes.dashboard.path} component={DashboardController}/>
            <Route path={Routes.reports.path} component={ReportsController}/>
            <Route path={Routes.preliminary.path} component={PreliminaryReportsController}/>
            <Route path={Routes.createReport.path} component={PreliminaryReportController}/>
            <Route path={Routes.editReport.path} component={PreliminaryReportController}/>
            <Route path={Routes.investigations.path} component={InvestigationsController}/>
            <Route path={Routes.editInvestigation.path} component={InvestigationController}/>
        </Route>
    </Router>
), document.getElementById('content'));
