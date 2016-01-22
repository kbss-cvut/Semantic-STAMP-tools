/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var Investigation = require('./Investigation');
var InvestigationStore = require('../../stores/InvestigationStore');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');
var ReportDetailControllerMixin = require('../mixin/ReportDetailControllerMixin');

var InvestigationController = React.createClass({
    mixins: [
        Reflux.listenTo(InvestigationStore, 'onInvestigationStoreTrigger'),
        ReportDetailControllerMixin
    ],

    getInitialState: function () {
        return {
            loading: true,
            report: null
        }
    },

    componentWillMount: function () {
        if (this.props.params.reportKey) {
            // Find the report by key
            Actions.findInvestigation(this.props.params.reportKey);
        }
    },

    onInvestigationStoreTrigger: function (data) {
        if (data.action === Actions.findInvestigation) {
            this.onReportLoaded(data.investigation);
        } else {
            this.setState({revisions: data.revisions});
        }
    },

    onReportLoaded: function (report) {
        if (!report) {
            this.setState({loading: false});
        } else {
            Actions.loadInvestigationRevisions(report.occurrence.key);
            this.setState({report: assign({}, report), loading: false});
        }
    },

    onSuccess: function (key) {
        this.loadReport(key ? key : this.state.report.key);
    },

    loadReport: function (key) {
        this.setState({loading: true});
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.investigations}
        });
        Actions.findInvestigation(key);
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(Routes.editInvestigation.name);
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
            <Investigation investigation={this.state.report} loading={this.state.loading} handlers={handlers}
                           revisions={this.renderRevisionInfo()} readOnly={!this.isLatestRevision()}/>
        );
    }
});

module.exports = InvestigationController;
