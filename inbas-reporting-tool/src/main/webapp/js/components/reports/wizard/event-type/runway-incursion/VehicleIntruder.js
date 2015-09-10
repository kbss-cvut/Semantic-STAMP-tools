/**
 * Created by kidney on 6/22/15.
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;

var Select = require('../../../../Select');
var Input = require('../../../../Input');

var VehicleIntruder = React.createClass({
    render: function () {
        var statement = this.props.statement;
        var yesNoUnknownOptions = [
            {value: 'true', label: 'Yes'},
            {value: 'false', label: 'No'},
            {value: 'unknown', label: 'Unknown'}
        ];
        return (
            <Panel header='Vehicle Event' style={{margin: '15px 0px 0px 0px'}}>
                <div className='row'>
                    <div className='form-group col-xs-6'>
                        <Input type='text' label='Vehicle Type' name='vehicleType'
                               value={statement.intruder.vehicleType}
                               onChange={this.props.onChange} title='Type of aerodrome vehicle that intruded'/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='text' label='Vehicle Call Sign' name='callSign'
                               value={statement.intruder.callSign}
                               onChange={this.props.onChange}
                               title='Vehicle call sign aerodrome manual'/>
                    </div>
                    <div className='col-xs-6'>
                        <Input type='text' label='Organization/Aerodrome Department' name='organization'
                               value={statement.intruder.organization}
                               onChange={this.props.onChange}
                               title='Organization/Aerodrome department operating the vehicle'/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Select label='Is the vehicle controlled by an ATS unit?' name='isAtsUnit'
                                value={statement.intruder.isAtsUnit} onChange={this.props.onChange}
                                options={yesNoUnknownOptions}/>
                    </div>
                    <div className='col-xs-6'>
                        <Select label='Has the vehicle a radio installed/operational?' name='hasRadio'
                                value={statement.intruder.hasRadio} onChange={this.props.onChange}
                                options={yesNoUnknownOptions}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-12'>
                        <Input type='textarea' label='What was the vehicle doing on the Runway?' name='wasDoing'
                               value={statement.intruder.wasDoing} onChange={this.props.onChange}
                               title='What was the vehicle doing on the Runway?'/>
                    </div>
                </div>
            </Panel>
        );
    }
});

module.exports = VehicleIntruder;
