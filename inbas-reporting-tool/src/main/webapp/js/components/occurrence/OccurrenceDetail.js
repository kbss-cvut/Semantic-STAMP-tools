/**
 * @jsx
 */
'use strict';

var React = require('react');
var DateTimePicker = require('kbss-react-bootstrap-datetimepicker');
var assign = require('object-assign');

var Input = require('../Input');

var OccurrenceDetail = React.createClass({

    propTypes: {
        occurrence: React.PropTypes.object.isRequired,
        onAttributeChange: React.PropTypes.func.isRequired
    },

    getFullName: function (data) {
        if (!data) {
            return '';
        }
        return data.firstName + ' ' + data.lastName;
    },

    onChange: function (e) {
        var occurrence = assign({}, this.props.occurrence);
        occurrence[e.target.name] = e.target.value;
        this.props.onAttributeChange('occurrence', occurrence);
    },

    onStartChange: function (value) {
        this.onChange({target: {name: 'startTime', value: Number(value)}});
    },

    onEndChange: function (value) {
        this.onChange({target: {name: 'endTime', value: Number(value)}});
    },

    render: function () {
        var occurrence = this.props.occurrence;
        return (
            <div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='name' value={occurrence.name} onChange={this.onChange}
                               label='Headline*'
                               title='Short descriptive summary of the occurrence - this field is required'/>
                    </div>
                </div>

                <div className='row'>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>Occurrence Start</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={occurrence.startTime.toString()}
                                        onChange={this.onStartChange}
                                        inputProps={{title: 'Date and time when the event occurred', bsSize: 'small'}}/>
                    </div>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>Occurrence End</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={occurrence.endTime.toString()}
                                        onChange={this.onEndChange}
                                        inputProps={{title: 'Date and time when the event ended', bsSize: 'small'}}/>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = OccurrenceDetail;
