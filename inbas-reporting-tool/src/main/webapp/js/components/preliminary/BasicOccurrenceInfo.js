/**
 * @jsx
 */
'use strict';

var React = require('react');

var injectIntl = require('../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var Input = require('../Input');
var Utils = require('../../utils/Utils');
var OccurrenceClassification = require('../occurrence/OccurrenceClassification');
var OccurrenceDetail = require('../occurrence/OccurrenceDetail');
var I18nMixin = require('../../i18n/I18nMixin');

var BasicOccurrenceInfo = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        onAttributeChange: React.PropTypes.func.isRequired
    },

    getFullName: function (data) {
        if (!data) {
            return '';
        }
        return data.firstName + ' ' + data.lastName;
    },

    render: function () {
        var report = this.props.report;
        return (
            <div>
                <OccurrenceDetail report={this.props.report}
                                  onAttributeChange={this.props.onAttributeChange}/>

                <OccurrenceClassification onChange={this.props.onAttributeChange}
                                    report={report}/>

                <div className='row'>
                    {this.renderAuthor()}
                    <div className='col-xs-4'>
                        {this.props.revisions}
                    </div>
                </div>

                {this.renderLastEdited()}
            </div>
        );
    },

    renderAuthor: function () {
        var report = this.props.report;
        return report.isNew ? null : (
            <div className='col-xs-4'>
                <Input type='text' value={this.getFullName(report.author)} label={this.i18n('author')}
                       title={this.i18n('author-title')}
                       disabled/>
            </div>);
    },

    renderLastEdited: function () {
        var report = this.props.report;
        if (report.isNew || !report.lastEdited) {
            return null;
        }
        var formattedDate = Utils.formatDate(new Date(report.lastEdited));
        return (
            <div className='form-group notice-small'>
                <FormattedMessage id='preliminary.detail.last-edited-msg'
                                  values={{date: formattedDate, name: this.getFullName(report.lastEditedBy)}}/>
            </div>
        );
    }
});

module.exports = injectIntl(BasicOccurrenceInfo);
