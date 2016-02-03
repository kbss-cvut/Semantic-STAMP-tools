'use strict';

var React = require('react');

var I18nMixin = require('../../i18n/I18nMixin');
var injectIntl = require('../../utils/injectIntl');
var Mask = require('../Mask');
var ReportType = require('../../model/ReportType');
var ResourceNotFound = require('../ResourceNotFound');

var Report = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        report: React.PropTypes.object,
        revisions: React.PropTypes.array,
        loading: React.PropTypes.bool
    },

    render: function () {
        if (this.props.loading) {
            return (
                <Mask text={this.i18n('detail.loading')}/>
            );
        }
        if (!this.props.report) {
            return (<ResourceNotFound resource={this.i18n('detail.not-found.title')}/>);
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var report = this.props.report,
            detailComponent = ReportType.getDetailController(report);

        return React.createElement(detailComponent, {
            report: report,
            revisions: this.props.revisions
        });
    }
});

module.exports = injectIntl(Report);
