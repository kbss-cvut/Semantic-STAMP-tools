/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var ReportDetail = require('./ReportDetail');
var ReportsStore = require('../../stores/ReportsStore');
var UserStore = require('../../stores/UserStore');
var router = require('../../utils/router');
var RouterStore = require('../../stores/RouterStore');

var ReportDetailController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange'),
        Reflux.listenTo(UserStore, 'onUserChange')],

    getInitialState: function () {
        var isNew = !this.props.params.reportKey;
        return {
            user: UserStore.getCurrentUser(),
            report: isNew ? this.initNewReport() : null,
            loading: !isNew
        }
    },

    initNewReport: function () {
        var report = RouterStore.getTransitionPayload('report_new');
        if (!report) {
            report = {};
        }
        report.occurrenceTime = Date.now();
        return report;
    },

    componentWillMount: function () {
        if (this.props.params.reportKey) {
            // Find the report by key
            Actions.findReport(this.props.params.reportKey);
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
        router.transitionTo(this.props.query.onSuccess);
    },

    onCancel: function () {
        router.transitionTo(this.props.query.onCancel);
    },


    render: function () {
        return (
            <ReportDetail report={this.state.report} loading={this.state.loading} user={this.state.user}
                          onCancel={this.onCancel} onSuccess={this.onSuccess} onChange={this.onChange}/>
        );
    }
});

module.exports = ReportDetailController;
