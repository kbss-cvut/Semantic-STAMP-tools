/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var Investigation = require('./Investigation');
var ReportsStore = require('../../stores/ReportsStore');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');

var InvestigationController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportLoaded')
    ],

    getInitialState: function () {
        return {
            loading: true,
            investigation: null
        }
    },

    componentWillMount: function () {
        if (this.props.params.reportKey) {
            // Find the report by key
            Actions.findReport(this.props.params.reportKey);
        }
    },

    onReportLoaded: function (report) {
        if (report === null) {
            this.setState({loading: false});
        } else if (report.key && report.key === this.props.params.reportKey) {
            this.setState({investigation: assign({}, report), loading: false});
        }
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(Routes.editInvestigation.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        }
    },

    onChange: function (attribute, value) {
        this.state.investigation[attribute] = value;   // Using [] notation because the att name is in variable
        this.setState({investigation: this.state.investigation}); // Force update
    },

    render: function () {
        return (
            <Investigation investigation={this.state.investigation} loading={this.state.loading}
                           onChange={this.onChange} onCancel={this.onCancel}/>
        );
    }
});

module.exports = InvestigationController;
