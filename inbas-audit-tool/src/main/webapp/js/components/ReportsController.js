/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var ReportsStore = require('../stores/ReportsStore');
var UserStore = require('../stores/UserStore');
var Reports = require('./Reports');
var Actions = require('../actions/Actions');

var ReportsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange'),
        Reflux.listenTo(UserStore, 'onChange')],
    getInitialState: function () {
        return {
            user: UserStore.getCurrentUser().user,
            reports: ReportsStore.getReports(),
            editedReport: null,
            editing: false
        };
    },
    componentWillMount: function () {
        Actions.loadReports({});
        Actions.loadUser({});
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
        this.setState(assign({}, this.state, {editing: true}))
    },
    onEditReport: function (report) {
        this.setState(assign({}, this.state, {
            editing: true,
            editedReport: report
        }));
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
            createReport: this.onCreateReport,
            startEdit: this.onEditReport,
            cancelEdit: this.onEditCancel
        };
        return (
            <Reports reports={this.state.reports} user={this.state.user} edit={edit}/>
        );
    }
});

module.exports = ReportsController;
