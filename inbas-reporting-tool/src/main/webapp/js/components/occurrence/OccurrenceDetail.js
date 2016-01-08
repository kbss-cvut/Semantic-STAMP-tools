/**
 * @jsx
 */
'use strict';

var React = require('react');
var DateTimePicker = require('kbss-react-bootstrap-datetimepicker');
var assign = require('object-assign');

var injectIntl = require('../../utils/injectIntl');

var Input = require('../Input');
var I18nMixin = require('../../i18n/I18nMixin');

var OccurrenceDetail = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired,
        onAttributeChange: React.PropTypes.func.isRequired
    },

    onChange: function (e) {
        var occurrence = assign({}, this.props.report.occurrence);
        occurrence[e.target.name] = e.target.value;
        this.props.onAttributeChange('occurrence', occurrence);
    },

    onStartChange: function (value) {
        this.props.onAttributeChange('occurrenceStart', Number(value));
    },

    onEndChange: function (value) {
        this.props.onAttributeChange('occurrenceEnd', value);
    },

    render: function () {
        var report = this.props.report;
        return (
            <div>
                <div className='row'>
                    <div className='col-xs-4'>
                        <Input type='text' name='name' value={report.occurrence.name} onChange={this.onChange}
                               label={this.i18n('headline') + '*'}
                               title={this.i18n('occurrence.headline-tooltip')}/>
                    </div>
                </div>

                <div className='row'>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>{this.i18n('occurrence.start-time')}</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={report.occurrenceStart.toString()}
                                        onChange={this.onStartChange}
                                        inputProps={{title: this.i18n('occurrence.start-time-tooltip'), bsSize: 'small'}}/>
                    </div>
                    <div className='picker-container form-group form-group-sm col-xs-4'>
                        <label className='control-label'>{this.i18n('occurrence.end-time')}</label>
                        <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={report.occurrenceEnd.toString()}
                                        onChange={this.onEndChange}
                                        inputProps={{title: this.i18n('occurrence.end-time-tooltip'), bsSize: 'small'}}/>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = injectIntl(OccurrenceDetail);
