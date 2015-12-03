/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var ReportDetail = require('./ReportDetail');
var ReportsStore = require('../../stores/PreliminaryReportStore');
var UserStore = require('../../stores/UserStore');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');
var PreliminaryReportFactory = require('../../model/PreliminaryReportFactory');

var ReportDetailController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange'),
        Reflux.listenTo(UserStore, 'onUserChange')
    ],

    getInitialState: function () {
        var isNew = !this.props.params.reportKey;
        return {
            user: UserStore.getCurrentUser(),
            report: isNew ? this.initNewReport() : null,
            loading: !isNew
        }
    },

    initNewReport: function () {
        var payload = RouterStore.getTransitionPayload(Routes.createReport.name),
            report = PreliminaryReportFactory.createReport();
        if (payload) {
            report.initialReports = payload.initialReports;
        }
        return report;
    },

    componentWillMount: function () {
        if (this.props.params.reportKey) {
            // Find the report by key
            Actions.findPreliminary(this.props.params.reportKey);
        }
    },

    onReportsChange: function (report) {
        if (report === null) {
            this.setState({loading: false});
        } else if (report.key && report.key === this.props.params.reportKey) {
            this.setState({report: report, loading: false});
        }
    },

    onUserChange: function () {
        this.setState({user: UserStore.getCurrentUser()});
    },

    onChange: function (attribute, value) {
        this.state.report[attribute] = value;   // Using [] notation because the att name is in variable
        this.setState({report: this.state.report}); // Force update
    },

    onSuccess: function () {
        if (this.state.report.isNew) {
            Routing.transitionTo(Routes.preliminary);
        } else {
            Actions.findPreliminary(this.state.report.key);
        }
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(this.state.report.isNew ? Routes.createReport.name : Routes.editReport.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Constants.HOME_ROUTE);
        }
    },


    render: function () {
        return (
            <ReportDetail report={this.state.report} loading={this.state.loading} user={this.state.user}
                          onCancel={this.onCancel} onSuccess={this.onSuccess} onChange={this.onChange}/>
        );
    }
});

module.exports = ReportDetailController;
