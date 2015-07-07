/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var React = require('react');
var Router = require('react-router');
var DefaultRoute = Router.DefaultRoute;
var Route = Router.Route;

var MainView = require('./../components/MainView');
var Home = require('./../components/Home');
var ReportsController = require('./../components/reports/ReportsController');
var ReportDetail = require('./../components/reports/ReportDetail');

var Routes = (
    <Route handler={MainView} path='/'>
        <Route name='home' path='home' handler={Home}/>
        <Route name='reports' path='reports' handler={ReportsController}/>
        <Route name='report' path='reports/report/:reportKey' handler={ReportDetail}/>
        <DefaultRoute handler={Home}/>
    </Route>
);

module.exports = Routes;
