/**
 * @jsx
 */
'use strict';

var React = require('react');

var injectIntl = require('../../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var Department = require('./Department').default;
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

    render: function () {
        var report = this.props.report;
        return (
            <div>
                <OccurrenceDetail report={report} onChange={this.props.onChange}/>

                <OccurrenceClassification onChange={this.props.onChange} report={report}/>

                <div className='row'>
                    <div className='col-xs-4'>
                        <Department report={report} onChange={this.props.onChange}/>
                    </div>
                </div>

                {this._renderProvenanceInfo()}
            </div>
        );
    },

    _renderProvenanceInfo: function () {
        var report = this.props.report;
        if (report.isNew) {
            return null;
        }
        var author = report.author ? report.author.firstName + ' ' + report.author.lastName : '',
            created = Utils.formatDate(new Date(report.dateCreated)),
            lastEditor, lastModified;
        if (!report.lastModified) {
            return (
                <div className='form-group notice-small'>
                    <FormattedMessage id='report.created-by-msg'
                                      values={{date: created, name: <b>{author}</b>}}/>
                </div>
            );
        }
        lastEditor = report.lastModifiedBy ? report.lastModifiedBy.firstName + ' ' + report.lastModifiedBy.lastName : '',
            lastModified = Utils.formatDate(new Date(report.lastModified));
        return (
            <div className='form-group notice-small'>
                <FormattedMessage id='report.created-by-msg'
                                  values={{date: created, name: <b>{author}</b>}}/>
                &nbsp;
                <FormattedMessage id='report.last-edited-msg'
                                  values={{date: lastModified, name: <b>{lastEditor}</b>}}/>
            </div>
        );
    }
});

module.exports = injectIntl(BasicOccurrenceInfo);
