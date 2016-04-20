/**
 * @jsx
 */
'use strict';

var React = require('react');

var injectIntl = require('../../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var Input = require('../../Input');
var Utils = require('../../../utils/Utils');
var OccurrenceClassification = require('./OccurrenceClassification');
var OccurrenceDetail = require('./Occurrence');
var I18nMixin = require('../../../i18n/I18nMixin');

var BasicOccurrenceInfo = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
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
                <OccurrenceDetail report={this.props.report} onChange={this.props.onChange}/>

                <OccurrenceClassification onChange={this.props.onChange} report={report}/>

                <div className='row'>
                    {this.renderAuthor()}
                    <div className='col-xs-4'>
                        {this.props.revisions}
                    </div>
                </div>

                {this._renderLastModified()}
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

    _renderLastModified: function () {
        var report = this.props.report;
        if (report.isNew || !report.lastModified) {
            return null;
        }
        var formattedDate = Utils.formatDate(new Date(report.lastModified));
        return (
            <div className='form-group notice-small'>
                <FormattedMessage id='report.last-edited-msg'
                                  values={{date: formattedDate, name: this.getFullName(report.lastModifiedBy)}}/>
            </div>
        );
    }
});

module.exports = injectIntl(BasicOccurrenceInfo);
