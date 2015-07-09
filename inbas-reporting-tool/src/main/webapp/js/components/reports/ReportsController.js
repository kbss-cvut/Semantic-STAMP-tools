/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var ReportsStore = require('../../stores/ReportsStore');
var UserStore = require('../../stores/UserStore');
var Reports = require('./Reports');
var Actions = require('../../actions/Actions');

var router = require('../../utils/router');

var ReportsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange'),
        Reflux.listenTo(UserStore, 'onChange')],
    getInitialState: function () {
        return {
            user: UserStore.getCurrentUser(),
            reports: ReportsStore.getReports(),
            editedReport: null,
            editing: false
        };
    },
    onReportsChange: function (newState) {
        newState.editing = false;
        newState.editedReport = null;
        this.onChange(newState);
    },
    onChange: function (newState) {
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
            <Reports reports={this.state.reports} user={this.state.user} edit={edit}/>
        );
    }
});

module.exports = ReportsController;
