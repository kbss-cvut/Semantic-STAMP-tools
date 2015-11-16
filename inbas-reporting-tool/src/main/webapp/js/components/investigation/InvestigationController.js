/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');
var Alert = require('react-bootstrap').Alert;

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var Investigation = require('./Investigation');
var InvestigationStore = require('../../stores/InvestigationStore');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');

var InvestigationController = React.createClass({
    mixins: [
        Reflux.listenTo(InvestigationStore, 'onReportLoaded')
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
            Actions.findInvestigation(this.props.params.reportKey);
        }
    },

    onReportLoaded: function (report) {
        if (report === null) {
            this.setState({loading: false});
        } else if (report.key && report.key === this.props.params.reportKey) {
            this.setState({investigation: assign({}, report), loading: false});
        }
    },

    onSuccess: function () {
        this.setState({message: 'Investigation successfully updated.'});
        Actions.findInvestigation(this.state.investigation.key);
    },

    dismissMessage: function () {
        this.setState({message: null});
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(Routes.editInvestigation.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Constants.HOME_ROUTE);
        }
    },

    onChange: function (attribute, value) {
        this.state.investigation[attribute] = value;   // Using [] notation because the att name is in variable
        this.setState({investigation: this.state.investigation}); // Force update
    },

    render: function () {
        return (
            <div>
                <Investigation investigation={this.state.investigation} loading={this.state.loading}
                               onChange={this.onChange} onSuccess={this.onSuccess} onCancel={this.onCancel}/>
                {this.renderMessage()}
            </div>
        );
    },

    renderMessage: function () {
        return this.state.message ? (
            <div className='form-group'>
                <Alert bsStyle='success' onDismiss={this.dismissMessage} dismissAfter={5000}>
                    <p>{this.state.message}</p>
                </Alert>
            </div>
        ) : null;
    }
});

module.exports = InvestigationController;
