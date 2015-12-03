/**
 * @jsx
 */
'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var ReportStore = require('../../stores/ReportStore');
var Reports = require('./Reports');
var ReportType = require('../../model/ReportType');
var Routes = require('../../utils/Routes');
var Routing = require('../../utils/Routing');

var ReportsController = React.createClass({
    mixins: [Reflux.listenTo(ReportStore, 'onReportsLoaded')],

    getInitialState: function () {
        return {
            reports: null
        };
    },

    componentDidMount: function () {
        Actions.loadAllReports();
    },

    onReportsLoaded: function (reports) {
        this.setState({reports: reports});
    },

    onEdit: function (report) {
        var route = ReportType.getDetailRoute(report);
        Routing.transitionTo(route, {
            params: {reportKey: report.key},
            handlers: {onCancel: Routes.reports}
        });
    },

    onRemove: function (report) {
        Actions.deleteReport(report);
    },

    render: function () {
        var actions = {
            onEdit: this.onEdit,
            onRemove: this.onRemove
        };
        return (
            <Reports reports={this.state.reports} actions={actions}/>
        );
    }
});

module.exports = ReportsController;
