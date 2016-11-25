'use strict';

var React = require('react');

var Actions = require('../../../actions/Actions');
var ReportDetail = require('./AuditReport');
var ReportDetailControllerMixin = require('../../mixin/ReportDetailControllerMixin');
var Routing = require('../../../utils/Routing');
var Routes = require('../../../utils/Routes');
var RouterStore = require('../../../stores/RouterStore');

var AuditReportController = React.createClass({
    propTypes: {
        report: React.PropTypes.object.isRequired,
        revisions: React.PropTypes.array
    },

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
        const handlers = RouterStore.getViewHandlers(this.props.report.isNew ? Routes.createReport.name : Routes.editReport.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Routes.reports);
        }
    },

    _resolveReadOnlyProps: function () {
        let roProps = {};
        roProps.readOnly = !this.isLatestRevision();
        if (this.props.report.isSafaReport()) {
            roProps.readOnly = true;
            roProps.readOnlyMessage = 'audit.safa.readonly.message';
        }
        return roProps;
    },


    render: function () {
        const handlers = {
            onChange: this.onChange,
            onSuccess: this.onSuccess,
            onCancel: this.onCancel,
            onRemove: this.onRemove
        };
        return <ReportDetail report={this.props.report} handlers={handlers} revisions={this.renderRevisionInfo()}
                             {...this._resolveReadOnlyProps()}/>;
    }
});

module.exports = AuditReportController;
