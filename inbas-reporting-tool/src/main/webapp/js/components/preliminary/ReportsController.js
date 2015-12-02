/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/PreliminaryReportStore');
var Reports = require('./Reports');

var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');

var ReportsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange')
    ],
    getInitialState: function () {
        return {
            reports: ReportsStore.getReports(),
            editedReport: null,
            editing: false
        };
    },
    componentWillMount: function () {
        Actions.loadReports();
    },
    onReportsChange: function (newState) {
        newState.editing = false;
        newState.editedReport = null;
        this.setState(assign({}, this.state, newState));
    },
    onCreateReport: function () {
        Routing.transitionTo(Routes.createReport, {handlers: {onSuccess: Routes.preliminary, onCancel: Routes.preliminary}});
    },
    onEditReport: function (report) {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: report.key},
            handlers: {onSuccess: Routes.preliminary, onCancel: Routes.preliminary}
        });
    },
    onEditCancel: function () {
        this.setState(assign({}, this.state, {
            editing: false,
            editedReport: null
        }));
    },


    render: function () {
        var edit = {
            editing: this.state.editing,
            editedReport: this.state.editedReport,
            onCreateReport: this.onCreateReport,
            onEditReport: this.onEditReport,
            onCancelEdit: this.onEditCancel
        };
        return (
            <Reports reports={this.state.reports} edit={edit}/>
        );
    }
});

module.exports = ReportsController;
