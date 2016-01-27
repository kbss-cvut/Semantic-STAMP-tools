/**
 * @jsx
 */

'use strict';

var React = require('react');

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var ReportDetail = require('./ReportDetail');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');
var ReportDetailControllerMixin = require('../mixin/ReportDetailControllerMixin');

var ReportDetailController = React.createClass({
    mixins: [
        ReportDetailControllerMixin
    ],

    onSuccess: function (reportKey) {
        if (this.props.report.isNew) {
            Routing.transitionTo(Routes.reports);
        } else {
            this.loadReport(reportKey ? reportKey : this.props.report.key);
        }
    },

    loadReport: function (key) {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.reports}
        });
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(this.props.report.isNew ? Routes.createReport.name : Routes.editReport.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Constants.HOME_ROUTE);
        }
    },

    onInvestigate: function () {
        Actions.createInvestigation(this.props.report.fileNumber, this.loadReport);
    },


    render: function () {
        var handlers = {
            onCancel: this.onCancel,
            onSuccess: this.onSuccess,
            onInvestigate: this.onInvestigate,
            onChange: this.onChange
        };
        return (
            <ReportDetail report={this.props.report} handlers={handlers} revisions={this.renderRevisionInfo()}
                          readOnly={!this.isLatestRevision()}/>
        );
    }
});

module.exports = ReportDetailController;
