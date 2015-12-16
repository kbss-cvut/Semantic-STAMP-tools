/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var injectIntl = require('../../../../../utils/injectIntl');

var Select = require('../../../../Select');
var Input = require('../../../../Input');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var VehicleIntruder = React.createClass({
    mixins: [I18nMixin],

    render: function () {
        var statement = this.props.statement;
        var yesNoUnknownOptions = [
            {value: 'true', label: this.i18n('yes')},
            {value: 'false', label: this.i18n('no')},
            {value: 'unknown', label: this.i18n('unknown')}
        ];
        return (
            <Panel header={this.i18n('eventtype.incursion.intruder.vehicle.panel-title')}
                   style={{margin: '15px 0px 0px 0px'}}>
                <div className='row'>
                    <div className='form-group col-xs-6'>
                        <Input type='text' label={this.i18n('eventtype.incursion.intruder.vehicle.type')}
                               name='vehicleType'
                               value={statement.intruder.vehicleType}
                               onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.vehicle.type-tooltip')}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='text' label={this.i18n('eventtype.incursion.intruder.vehicle.callsign')}
                               name='callSign'
                               value={statement.intruder.callSign}
                               onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.vehicle.callsign-tooltip')}/>
                    </div>
                    <div className='col-xs-6'>
                        <Input type='text' label={this.i18n('eventtype.incursion.intruder.organization')}
                               name='organization'
                               value={statement.intruder.organization}
                               onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.vehicle.organization-tooltip')}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Select label={this.i18n('eventtype.incursion.intruder.vehicle.isats')} name='isAtsUnit'
                                value={statement.intruder.isAtsUnit} onChange={this.props.onChange}
                                options={yesNoUnknownOptions}/>
                    </div>
                    <div className='col-xs-6'>
                        <Select label={this.i18n('eventtype.incursion.intruder.vehicle.hasradio')} name='hasRadio'
                                value={statement.intruder.hasRadio} onChange={this.props.onChange}
                                options={yesNoUnknownOptions}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-12'>
                        <Input type='textarea' label={this.i18n('eventtype.incursion.intruder.vehicle.wasdoing')}
                               name='wasDoing'
                               value={statement.intruder.wasDoing} onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.vehicle.type')}/>
                    </div>
                </div>
            </Panel>
        );
    }
});

module.exports = injectIntl(VehicleIntruder);
