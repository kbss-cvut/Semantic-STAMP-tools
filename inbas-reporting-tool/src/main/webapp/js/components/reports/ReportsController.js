/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/ReportsStore');
var Reports = require('./Reports');

var router = require('../../utils/router');

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
    componentWillMount: function() {
        Actions.loadReports();
    },
    onReportsChange: function (newState) {
        newState.editing = false;
        newState.editedReport = null;
        this.setState(assign({}, this.state, newState));
    },
    onCreateReport: function () {
        router.transitionTo('report_new', null, {onSuccess: 'reports', onCancel: 'reports'});
    },
    onEditReport: function (report) {
        router.transitionTo('report', {reportKey: report.key}, {onSuccess: 'reports', onCancel: 'reports'});
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
