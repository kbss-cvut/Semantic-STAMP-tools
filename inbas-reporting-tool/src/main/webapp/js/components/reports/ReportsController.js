/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/ReportsStore');
var Reports = require('./Reports');

var Routing = require('../../utils/Routing');

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
        Routing.transitionTo('reports/create', null, null, {onSuccess: 'reports', onCancel: 'reports'});
    },
    onEditReport: function (report) {
        Routing.transitionTo('reports/' + report.key, null, null, {onSuccess: 'reports', onCancel: 'reports'});
    },
    onEditCancel: function () {
        this.setState(assign({}, this.state, {
            editing: false,
            editedReport: null
        }));
    },


    render: function () {
        if (this.props.children) {
            return this.props.children;
        }
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
