'use strict';

var React = require('react');
var Router = require('react-router');
var DefaultRoute = Router.DefaultRoute;
var Route = Router.Route;

var Login = require('./../components/login/Login');
var Register = require('./../components/register/Register');
var MainView = require('./../components/MainView');
var DashboardController = require('../components/dashboard/DashboardController');
var ReportsController = require('./../components/reports/ReportsController');
var ReportDetailController = require('./../components/reports/ReportDetailController');
var InvestigationsController = require('../components/investigation/InvestigationsController');
var InvestigationController = require('./../components/investigation/InvestigationController');

var Routes = (
    <Route handler={MainView} path='/'>
        <Route name='login' path='login' handler={Login}/>
        <Route name='register' path='register' handler={Register}/>
        <Route name='dashboard' path='dashboard' handler={DashboardController}/>
        <Route name='reports' path='reports' handler={ReportsController}/>
        <Route name='report' path='reports/report/:reportKey' handler={ReportDetailController}/>
        <Route name='report_new' path='reports/create' handler={ReportDetailController}/>
        <Route name='investigations' path='investigations' handler={InvestigationsController}/>
        <Route name='investigation' path='investigations/investigation/:reportKey' handler={InvestigationController}/>
        <DefaultRoute handler={DashboardController}/>
    </Route>
);

module.exports = Routes;
