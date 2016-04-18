/**
 * @jsx
 */
'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var DataFilter = require('../../utils/DataFilter');
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

    onReportsLoaded: function (data) {
        if (data.action === Actions.loadAllReports) {
            this.setState({reports: data.reports});
        }
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
        return DataFilter.filterData(this.state.reports, this.state.filter);
    },


    render: function () {
        var actions = {
            onEdit: this.onEdit,
            onRemove: this.onRemove,
            onFilterChange: this.onFilterChange
        };
        return (
            <Reports allReports={this.state.reports} reports={this.filterReports()} filter={this.state.filter}
                     actions={actions}/>
        );
    }
});

module.exports = ReportsController;
