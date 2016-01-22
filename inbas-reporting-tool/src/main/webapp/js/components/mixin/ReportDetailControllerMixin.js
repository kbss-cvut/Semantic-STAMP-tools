'use strict';

var React = require('react');
var assign = require('object-assign');

var RevisionInfo = require('../reports/RevisionInfo');

/**
 * Aggregates some of the methods that are common to both Investigation and Preliminary report detail controllers.
 */
var ReportDetailControllerMixin = {

    onChange: function (changes) {
        var report = assign(this.state.report, changes);
        this.setState({report: report}); // Force update
    },

    onRevisionSelected: function (revision) {
        this.loadReport(revision.key);
    },

    isLatestRevision: function () {
        var revisions = this.state.revisions;
        if (!revisions || revisions.length === 0) {
            return true;
        }
        return this.state.report.revision === revisions[0].revision;
    },


    renderRevisionInfo: function () {
        if (!this.state.revisions || this.state.revisions.length === 0) {
            return null;
        }
        return (<RevisionInfo revisions={this.state.revisions} selectedRevision={this.state.report.revision}
                              onSelect={this.onRevisionSelected}/>);
    }
};

module.exports = ReportDetailControllerMixin;
