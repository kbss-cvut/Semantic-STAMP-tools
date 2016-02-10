/**
 * @jsx
 */

'use strict';

var React = require('react');

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var Investigation = require('./Investigation');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');
var ReportDetailControllerMixin = require('../mixin/ReportDetailControllerMixin');

var InvestigationController = React.createClass({
    mixins: [
        ReportDetailControllerMixin
    ],

    onSuccess: function (key) {
        if (!key || key === this.props.report.key) {
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
            Routing.transitionTo(Constants.HOME_ROUTE);
        }
    },


    render: function () {
        var handlers = {
            onChange: this.onChange,
            onSuccess: this.onSuccess,
            onCancel: this.onCancel
        };
        return (
            <Investigation investigation={this.props.report} handlers={handlers} revisions={this.renderRevisionInfo()}
                           readOnly={!this.isLatestRevision()}/>
        );
    }
});

module.exports = InvestigationController;
