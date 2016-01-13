/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var Investigation = require('./Investigation');
var InvestigationStore = require('../../stores/InvestigationStore');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var RouterStore = require('../../stores/RouterStore');
var RevisionInfo = require('../reports/RevisionInfo');

var InvestigationController = React.createClass({
    mixins: [
        Reflux.listenTo(InvestigationStore, 'onInvestigationStoreTrigger')
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

    onInvestigationStoreTrigger: function (data) {
        if (data.action === Actions.findInvestigation) {
            this.onReportLoaded(data.investigation);
        } else {
            this.onRevisionsLoaded(data.revisions);
        }
    },

    onReportLoaded: function (report) {
        if (!report) {
            this.setState({loading: false});
        } else {
            Actions.loadInvestigationRevisions(report.occurrence.key);
            this.setState({investigation: assign({}, report), loading: false});
        }
    },

    onRevisionsLoaded: function (revisions) {
        this.setState({revisions: revisions});
    },

    onSuccess: function (key) {
        this.loadInvestigation(key ? key : this.state.investigation.key);
    },

    loadInvestigation: function (key) {
        this.setState({loading: true});
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.investigations}
        });
        Actions.findInvestigation(key);
    },

    onCancel: function () {
        var handlers = RouterStore.getViewHandlers(Routes.editInvestigation.name);
        if (handlers) {
            Routing.transitionTo(handlers.onCancel);
        } else {
            Routing.transitionTo(Constants.HOME_ROUTE);
        }
    },

    onChange: function (values) {
        var investigation = assign(this.state.investigation, values);
        this.setState({investigation: investigation}); // Force update
    },

    onRevisionSelected: function (revision) {
        this.loadInvestigation(revision.key);
    },

    isLastRevision: function () {
        if (!this.state.investigation || !this.state.revisions || this.state.revisions.length === 0) {
            return true;
        }
        return this.state.investigation.revision === this.state.revisions[0].revision;
    },


    render: function () {
        var handlers = {
            onChange: this.onChange,
            onSuccess: this.onSuccess,
            onCancel: this.onCancel
        };
        return (
            <Investigation investigation={this.state.investigation} loading={this.state.loading} handlers={handlers}
                           revisions={this.renderRevisionInfo()} readOnly={!this.isLastRevision()}/>
        );
    },

    renderRevisionInfo: function () {
        if (!this.state.revisions) {
            return null;
        }
        return (<RevisionInfo revisions={this.state.revisions} selectedRevision={this.state.investigation.revision}
                              onSelect={this.onRevisionSelected}/>);
    }
});

module.exports = InvestigationController;
