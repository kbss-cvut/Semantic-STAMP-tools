'use strict';

const React = require('react');
const assign = require('object-assign');

const Actions = require('../../actions/Actions');
const RevisionInfo = require('../report/RevisionInfo');
const Routes = require('../../utils/Routes');
const Routing = require('../../utils/Routing');

/**
 * Aggregates some of the methods that are common to all report detail controllers.
 */
const ReportDetailControllerMixin = {

    onChange: function (changes) {
        const report = assign(this.props.report, changes);
        this.setState({report: report}); // Force update
    },

    onRevisionSelected: function (revision) {
        this.loadReport(revision.key);
    },

    isLatestRevision: function () {
        const revisions = this.props.revisions;
        if (!revisions || revisions.length === 0) {
            return true;
        }
        return this.props.report.revision === revisions[0].revision;
    },

    onRemove: function (onError) {
        Actions.deleteReportChain(this.props.report.fileNumber, this.onRemoveSuccess, onError);
    },

    onRemoveSuccess: function () {
        Routing.transitionTo(Routes.reports);
    },


    renderRevisionInfo: function () {
        if (!this.props.revisions || this.props.revisions.length === 0) {
            return null;
        }
        return <RevisionInfo revisions={this.props.revisions} selectedRevision={this.props.report.revision}
                             onSelect={this.onRevisionSelected}/>;
    }
};

module.exports = ReportDetailControllerMixin;
