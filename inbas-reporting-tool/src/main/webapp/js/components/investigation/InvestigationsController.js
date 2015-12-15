/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var injectIntl = require('react-intl').injectIntl;

var Actions = require('../../actions/Actions');
var InvestigationRow = require('./InvestigationRow');
var InvestigationStore = require('../../stores/InvestigationStore');
var Reports = require('../reports/Reports');

var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var I18nMixin = require('../../i18n/I18nMixin');

var InvestigationsController = React.createClass({
    mixins: [
        Reflux.listenTo(InvestigationStore, 'onInvestigationsLoaded'),
        I18nMixin
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
            <Reports panelTitle={this.i18n('investigation.panel-title')} reports={this.state.investigations}
                     actions={actions}
                     rowComponent={InvestigationRow} tableHeader={this.renderTableHeader()}/>
        );
    },

    renderTableHeader: function () {
        return (
            <thead>
            <tr>
                <th className='col-xs-2'>{this.i18n('headline')}</th>
                <th className='col-xs-2'>{this.i18n('reports.table-date')}</th>
                <th className='col-xs-6'>{this.i18n('narrative')}</th>
                <th className='col-xs-2'>{this.i18n('actions')}</th>
            </tr>
            </thead>
        );
    }
});

module.exports = injectIntl(InvestigationsController);
