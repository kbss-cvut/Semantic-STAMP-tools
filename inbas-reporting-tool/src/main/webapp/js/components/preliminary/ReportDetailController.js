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
var RevisionInfo = require('../reports/RevisionInfo');

var ReportDetailController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportStoreTrigger'),
        Reflux.listenTo(UserStore, 'onUserChange')
    ],

    getInitialState: function () {
        var isNew = !this.props.params.reportKey;
        return {
            user: UserStore.getCurrentUser(),
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
            if (this.shouldLoadRevisions()) {
                Actions.loadPreliminaryRevisions(report.occurrence.key);
            }
            this.setState({report: report, loading: false});
        }
    },

    shouldLoadRevisions: function () {
        var revisions = this.state.revisions;
        if (!revisions) {
            return true;
        }
        for (var i = 0, len = revisions.length; i < len; i++) {
            if (revisions[i].key === this.state.report.key) {
                return false;
            }
        }
        return true;
    },

    onUserChange: function () {
        this.setState({user: UserStore.getCurrentUser()});
    },

    onChange: function (attribute, value) {
        this.state.report[attribute] = value;   // Using [] notation because the att name is in variable
        this.setState({report: this.state.report}); // Force update
    },

    onSuccess: function (reportKey) {
        if (this.state.report.isNew) {
            Routing.transitionTo(Routes.preliminary);
        } else {
            this.setState({loading: true});
            Actions.findPreliminary(reportKey ? reportKey : this.state.report.key);
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

    onInvestigate: function () {
        Actions.createInvestigation(this.state.report.key, this.openInvestigation);
    },

    openInvestigation: function (key) {
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.investigations}
        });
    },

    onRevisionSelected: function (revision) {
        this.setState({loading: true});
        Actions.findPreliminary(revision.key);
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
                          revisions={this.renderRevisionInfo()}/>
        );
    },

    renderRevisionInfo: function () {
        // Revisions not loaded yet or the report is new and has no revisions, yet
        if (!this.state.revisions || this.state.revisions.length === 0) {
            return null;
        }
        var revisions = this.state.revisions,
            selectedRevision = this.state.report.revision;
        return <RevisionInfo revisions={revisions} selectedRevision={selectedRevision}
                             onSelect={this.onRevisionSelected}/>;
    }
});

module.exports = ReportDetailController;
