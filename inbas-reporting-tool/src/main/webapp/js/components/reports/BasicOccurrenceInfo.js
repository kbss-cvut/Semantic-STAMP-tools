/**
 * @jsx
 */
'use strict';

var React = require('react');

var Input = require('../Input');
var Utils = require('../../utils/Utils');
var OccurrenceSeverity = require('./OccurrenceSeverity');
var OccurrenceDetail = require('../occurrence/OccurrenceDetail');

var BasicOccurrenceInfo = React.createClass({

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
                <OccurrenceDetail occurrence={this.props.report.occurrence}
                                  onAttributeChange={this.props.onAttributeChange}/>

                <div className='row'>
                    <div className='col-xs-4'>
                        <OccurrenceSeverity onChange={this.props.onAttributeChange}
                                            severityAssessment={report.severityAssessment}/>
                    </div>
                </div>

                {this.renderAuthor()}

                {this.renderLastEdited()}
            </div>
        );
    },

    renderAuthor: function () {
        var report = this.props.report;
        return report.isNew ? null : (
            <div className='row'>
                <div className='col-xs-4'>
                    <Input type='text' value={this.getFullName(report.author)} label='Author' title='Report author'
                           disabled/>
                </div>
            </div>);
    },

    renderLastEdited: function () {
        var report = this.props.report;
        if (report.isNew) {
            return null;
        }
        var formattedDate = Utils.formatDate(new Date(report.lastEdited));
        var text = 'Last edited on ' + formattedDate + ' by ' + this.getFullName(report.lastEditedBy) + '.';
        return (
            <div className='form-group italics'>
                {text}
            </div>
        );
    }
});

module.exports = BasicOccurrenceInfo;
