/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var InvestigationStore = require('../../stores/InvestigationStore');
var Investigations = require('./Investigations');
var ReportsTable = require('./../reports/ReportsTable');
var Mask = require('../Mask');

var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');

var InvestigationsController = React.createClass({
    mixins: [
        Reflux.listenTo(InvestigationStore, 'onInvestigationsLoaded')
    ],

    getInitialState: function () {
        return {
            investigations: null
        }
    },

    componentWillMount: function () {
        Actions.loadInvestigations();
    },

    onInvestigationsLoaded: function (data) {
        this.setState({investigations: data});
    },

    onEditInvestigation: function (investigation) {
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: investigation.key},
            handlers: {onCancel: Routes.investigations}
        });
    },

    render: function () {
        if (!this.state.investigations) {
            return (
                <Mask text='Loading investigations'/>
            );
        }
        return (
            <div>
                <Investigations investigations={this.state.investigations}
                                onEditInvestigation={this.onEditInvestigation}/>
            </div>
        );
    }
});

module.exports = InvestigationsController;
