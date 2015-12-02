'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var ReportStore = require('../../stores/ReportStore');
var Reports = require('./Reports');


var ReportsController = React.createClass({
    mixins: [Reflux.listenTo(ReportStore, 'onReportsLoaded')],

    getInitialState: function() {
        return {
            reports: null
        };
    },

    componentDidMount: function() {
        Actions.loadAllReports();
    },

    onReportsLoaded: function(reports) {
        this.setState({reports: reports});
    },

    render: function() {
        return (
            <Reports reports={this.state.reports} />
        );
    }
});

module.exports = ReportsController;
