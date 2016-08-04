'use strict';

var React = require('react');

var I18nMixin = require('../../../i18n/I18nMixin');
var MessageMixin = require('../../mixin/MessageMixin');
var ReportDetailMixin = require('../../mixin/ReportDetailMixin');

var SafetyIssueReport = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin],

    render: function () {
        return <div>Safety issue report</div>;
    }
});

module.exports = SafetyIssueReport;
