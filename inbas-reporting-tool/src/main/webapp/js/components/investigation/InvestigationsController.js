/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var InvestigationRow = require('./InvestigationRow');
var InvestigationStore = require('../../stores/InvestigationStore');
var Reports = require('../reports/Reports');

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

    onRemoveInvestigation: function (investigation) {
        Actions.deleteInvestigation(investigation);
    },

    render: function () {
        var actions = {
            onEdit: this.onEditInvestigation,
            onRemove: this.onRemoveInvestigation
        };
        return (
            <Reports panelTitle='Investigation reports' reports={this.state.investigations} actions={actions}
                     rowComponent={InvestigationRow} tableHeader={this.renderTableHeader()}/>
        );
    },

    renderTableHeader: function () {
        return (
            <thead>
            <tr>
                <th className='col-xs-2'>Occurrence headline</th>
                <th className='col-xs-2'>Occurrence date</th>
                <th className='col-xs-6'>Narrative</th>
                <th className='col-xs-2'>Actions</th>
            </tr>
            </thead>
        );
    }
});

module.exports = InvestigationsController;
