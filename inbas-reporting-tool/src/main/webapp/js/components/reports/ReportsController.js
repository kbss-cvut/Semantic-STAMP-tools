/**
 * @jsx
 */
'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var ReportStore = require('../../stores/ReportStore');
var Reports = require('./Reports');
var Routes = require('../../utils/Routes');
var Routing = require('../../utils/Routing');

var ReportsController = React.createClass({
    mixins: [Reflux.listenTo(ReportStore, 'onReportsLoaded')],

    getInitialState: function () {
        return {
            reports: null,
            filter: null
        };
    },

    componentDidMount: function () {
        Actions.loadAllReports();
    },

    onReportsLoaded: function (reports) {
        this.setState({reports: reports});
    },

    onEdit: function (report) {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: report.key},
            handlers: {onCancel: Routes.reports}
        });
    },

    onRemove: function (report) {
        Actions.deleteReportChain(report.fileNumber);
    },

    onFilterChange: function (filter) {
        this.setState({filter: assign({}, this.state.filter, filter)});
    },

    filterReports: function () {
        var filter = this.state.filter;
        if (!filter) {
            return this.state.reports;
        }
        return this.state.reports.filter(function (item) {
            for (var key in filter) {
                if (filter[key].toLowerCase() !== 'all' && item[key].toLowerCase() !== filter[key].toLowerCase()) {
                    return false;
                }
                return true;
            }
        });
    },


    render: function () {
        var actions = {
            onEdit: this.onEdit,
            onRemove: this.onRemove,
            onFilterChange: this.onFilterChange
        };
        return (
            <Reports reports={this.filterReports()} filter={this.state.filter} actions={actions}/>
        );
    }
});

module.exports = ReportsController;
