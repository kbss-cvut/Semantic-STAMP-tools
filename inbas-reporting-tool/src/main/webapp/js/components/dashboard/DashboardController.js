/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var Routing = require('../../utils/Routing');
var UserStore = require('../../stores/UserStore');
var ReportsStore = require('../../stores/ReportsStore');
var Dashboard = require('./Dashboard');
var WizardWindow = require('./../wizard/WizardWindow');
var InitialReportImportSteps = require('../initialreport/Steps');

var DashboardController = React.createClass({
    mixins: [
        Reflux.listenTo(UserStore, 'onUserLoaded'),
        Reflux.listenTo(ReportsStore, 'onReportsLoaded')
    ],
    getInitialState: function () {
        return {
            firstName: UserStore.getCurrentUser() ? UserStore.getCurrentUser().firstName : '',
            reports: ReportsStore.getReports() ? ReportsStore.getReports() : [],
            initialReportImportOpen: false
        }
    },

    componentWillMount: function () {
        Actions.loadReports();
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    onReportsLoaded: function () {
        this.setState({reports: ReportsStore.getReports()});
    },

    createEmptyReport: function () {
        Routing.transitionTo('reports/create', null, null, {onSuccess: 'reports', onCancel: 'dashboard'});
    },

    cancelInitialReportImport: function () {
        this.setState({initialReportImportOpen: false});
    },

    openInitialReportImport: function () {
        this.setState({initialReportImportOpen: true});
    },

    importInitialReport: function (data, closeCallback) {
        Routing.transitionTo('reports/create', null, {initialReports: [data.initialReport]}, {
            onSuccess: 'reports',
            onCancel: 'dashboard'
        });
        closeCallback();
    },

    openReport: function (report) {
        Routing.transitionTo('reports/' + report.key, null, {onSuccess: 'reports', onCancel: 'dashboard'});
    },

    showReports: function () {
        Routing.transitionTo('reports');
    },


    render: function () {
        var wizardProperties = {
            steps: InitialReportImportSteps,
            initialReport: {},
            title: 'Import Initial Report',
            onFinish: this.importInitialReport
        };
        return (
            <div>
                <WizardWindow show={this.state.initialReportImportOpen} {...wizardProperties}
                              onHide={this.cancelInitialReportImport}/>
                <Dashboard userFirstName={this.state.firstName} reports={this.state.reports}
                           showAllReports={this.showReports} createEmptyReport={this.createEmptyReport}
                           importInitialReport={this.openInitialReportImport} openReport={this.openReport}/>
            </div>
        );
    }
});

module.exports = DashboardController;
