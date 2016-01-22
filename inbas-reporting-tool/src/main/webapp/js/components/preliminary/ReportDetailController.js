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
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');
var PreliminaryReportFactory = require('../../model/PreliminaryReportFactory');
var ReportDetailControllerMixin = require('../mixin/ReportDetailControllerMixin');

var ReportDetailController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportStoreTrigger'),
        ReportDetailControllerMixin
    ],

    getInitialState: function () {
        var isNew = !this.props.params.reportKey;
        return {
            report: isNew ? this.initNewReport() : null,
            revisions: null,
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

    onReportStoreTrigger: function (data) {
        if (data.action === Actions.findPreliminary) {
            this.onReportLoaded(data.report);
        } else if (data.action === Actions.loadPreliminaryRevisions) {
            this.setState({revisions: data.revisions});
        }
    },

    onReportLoaded: function (report) {
        if (report === null) {
            this.setState({loading: false});
        } else {
            Actions.loadPreliminaryRevisions(report.occurrence.key);
            this.setState({report: report, loading: false});
        }
    },

    onSuccess: function (reportKey) {
        if (this.state.report.isNew) {
            Routing.transitionTo(Routes.preliminary);
        } else {
            this.loadReport(reportKey ? reportKey : this.state.report.key);
        }
    },

    loadReport: function (key) {
        this.setState({loading: true});
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.reports}
        });
        Actions.findPreliminary(key);
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(this.state.report.isNew ? Routes.createReport.name : Routes.editReport.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Constants.HOME_ROUTE);
        }
    },

    onInvestigate: function () {
        Actions.createInvestigation(this.state.report.key, this.openInvestigation);
    },

    openInvestigation: function (key) {
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.investigations}
        });
    },


    render: function () {
        var handlers = {
            onCancel: this.onCancel,
            onSuccess: this.onSuccess,
            onInvestigate: this.onInvestigate,
            onChange: this.onChange
        };
        return (
            <ReportDetail report={this.state.report} loading={this.state.loading} handlers={handlers}
                          revisions={this.renderRevisionInfo()} readOnly={!this.isLatestRevision()}/>
        );
    }
});

module.exports = ReportDetailController;
