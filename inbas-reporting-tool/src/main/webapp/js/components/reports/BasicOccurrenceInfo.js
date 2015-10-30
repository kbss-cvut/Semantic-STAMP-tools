/**
 * @jsx
 */
'use strict';

var React = require('react');
var DateTimePicker = require('kbss-react-bootstrap-datetimepicker');

var Input = require('../Input');
var Utils = require('../../utils/Utils');
var OccurrenceSeverity = require('./OccurrenceSeverity');

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

    onDateChange: function (value) {
        this.props.onAttributeChange('occurrenceTime', new Date(Number(value)));
    },

    render: function () {
        var report = this.props.report;
        return (
            <div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='name' value={report.name} onChange={this.props.onChange}
                               label='Occurrence Summary' title='Short descriptive summary of the occurrence'/>
                    </div>
                </div>

                <div className='row'>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>Occurrence Time</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={report.occurrenceTime.toString()}
                                        onChange={this.onDateChange}
                                        inputProps={{title: 'Date and time when the event occurred', bsSize: 'small'}}/>
                    </div>
                </div>

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
