'use strict';

var React = require('react');

var Actions = require('../../../actions/Actions');
var ReportDetail = require('./SafetyIssueReport');
var ReportDetailControllerMixin = require('../../mixin/ReportDetailControllerMixin');
var Routing = require('../../../utils/Routing');
var Routes = require('../../../utils/Routes');
var RouterStore = require('../../../stores/RouterStore');

var SafetyIssueReportController = React.createClass({
    mixins: [
        ReportDetailControllerMixin
    ],

    onSuccess: function (key) {
        if (this.props.report.isNew) {
            Routing.transitionTo(Routes.reports);
        } else if (!key || key === this.props.report.key) {
            Actions.loadReport(this.props.report.key);
        } else {
            this.loadReport(key);
        }
    },

    loadReport: function (key) {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.reports}
        });
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(Routes.editReport.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Routes.reports);
        }
    },


    render: function () {
        var handlers = {
            onChange: this.onChange,
            onSuccess: this.onSuccess,
            onCancel: this.onCancel
        };
        return (
            <ReportDetail report={this.props.report} handlers={handlers} revisions={this.renderRevisionInfo()}
                          readOnly={!this.isLatestRevision()}/>
        );
    }
});

module.exports = SafetyIssueReportController;
