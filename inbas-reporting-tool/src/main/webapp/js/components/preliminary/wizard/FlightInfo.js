/**
 * @jsx
 */

'use strict';

var React = require('react');
var injectIntl = require('../../../utils/injectIntl');

var Input = require('../../Input');
var I18nMixin = require('../../../i18n/I18nMixin');

var FlightInfo = React.createClass({
    mixins: [I18nMixin],

    render: function () {
        return (
            <div className='row'>
                <div className='col-xs-6'>
                    <Input type='text' label={this.i18n('flight.departure')} name='lastDeparturePoint'
                           onChange={this.props.onChange} value={this.props.lastDeparturePoint}/>
                </div>
                <div className='col-xs-6'>
                    <Input type='text' label={this.i18n('flight.destination')} name='plannedDestination'
                           onChange={this.props.onChange} value={this.props.plannedDestination}/>
                </div>
            </div>
        );
    }
});

module.exports = injectIntl(FlightInfo);
