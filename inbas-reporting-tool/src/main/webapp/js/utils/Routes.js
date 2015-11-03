'use strict';

var React = require('react');
var Route = require('react-router').Route;
var IndexRoute = require('react-router').IndexRoute;

var Login = require('./../components/login/Login');
var Register = require('./../components/register/Register');
var MainView = require('./../components/MainView');
var DashboardController = require('../components/dashboard/DashboardController');
var ReportsController = require('./../components/reports/ReportsController');
var ReportDetailController = require('./../components/reports/ReportDetailController');
var InvestigationsController = require('../components/investigation/InvestigationsController');
var InvestigationController = require('./../components/investigation/InvestigationController');

var Routes = (
    <Route path='/' component={MainView}>
        <IndexRoute component={DashboardController}/>
        <Route path='login' component={Login}/>
        <Route path='register' component={Register}/>
        <Route path='dashboard' component={DashboardController}/>
        <Route path='reports' component={ReportsController}/>
        <Route path='reports/create' component={ReportDetailController}/>
        <Route path='reports/:reportKey' component={ReportDetailController}/>
        <Route path='investigations' component={InvestigationsController}/>
        <Route path='investigations/:reportKey' component={InvestigationController}/>
    </Route>
);

module.exports = Routes;
