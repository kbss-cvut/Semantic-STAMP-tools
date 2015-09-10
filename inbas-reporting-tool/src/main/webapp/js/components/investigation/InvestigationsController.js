'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/ReportsStore');
var Investigations = require('./Investigations');
var ReportsTable = require('./../reports/ReportsTable');
var Mask = require('../Mask');

var InvestigationsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsLoaded')
    ],

    getInitialState: function () {
        return {
            reports: null,
            investigations: []
        }
    },

    componentWillMount: function () {
        Actions.loadInvestigations();
        Actions.loadReports();
    },

    onReportsLoaded: function (data) {
        this.setState({reports: data.reports});
    },

    /**
     * Gets reports for investigation, i.e. those which are not already being investigated.
     */
    getReportsToInvestigate: function () {
        var reports = this.state.reports;
        var investigations = this.state.investigations;
        var len = investigations.length;
        return reports.filter(function (report) {
            for (var i = 0; i < len; i++) {
                if (investigations[i].report.key === report.key) {
                    return false;
                }
            }
            return true;
        });
    },

    render: function () {
        if (!this.state.reports) {
            return (
                <Mask text='Loading reports and investigations'/>
            );
        }
        var style = {height: '40vh'};
        return (
            <div>
                <Investigations investigations={this.state.investigations} style={style}/>
                <ReportsTable reports={this.getReportsToInvestigate()} style={style}/>
            </div>
        );
    }
});

module.exports = InvestigationsController;
