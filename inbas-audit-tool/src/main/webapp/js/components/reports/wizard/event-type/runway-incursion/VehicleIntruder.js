/**
 * Created by kidney on 6/22/15.
 */

'use strict';

var React = require('react');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var VehicleIntruder = React.createClass({
    // TODO Checkbox onChange
    render: function () {
        var statement = this.props.statement;
        return (
            <Panel header='Vehicle Event' style={{margin: '15px 0px 0px 0px'}}>
                <div className='form-group report-detail'>
                    <Input type='text' label='Vehicle Type' name='vehicleType' value={statement.intruder.vehicleType}
                           onChange={this.props.onChange} title='Type of aerodrome vehicle that intruded'/>
                </div>
                <div className='form-group float-container'>
                    <div className='report-detail-float'>
                        <Input type='text' label='Vehicle Call Sign' name='callSign'
                               value={statement.intruder.callSign}
                               onChange={this.props.onChange}
                               title='Vehicle call sign aerodrome manual'/>
                    </div>
                    <div className='report-detail-float-right'>
                        <Input type='text' label='Organization/Aerodrome Department' name='operator'
                               value={statement.intruder.operator}
                               onChange={this.props.onChange}
                               title='Organization/Aerodrome department operating the vehicle'/>
                    </div>
                </div>
                <div className='form-group float-container'>
                    <div className='report-detail-float'>
                        <Input type='checkbox' name='isAtsUnit' label='Is vehicle controlled by ATS unit?'
                               checked={statement.intruder.isAtsUnit} onChange={this.props.onChange}
                               title='Is the vehicle controlled by an ATS unit'/>
                    </div>
                    <div className='report-detail-float-right'>
                        <Input type='checkbox' name='hasRadio' label='Has vehicle radio installed/operational?'
                               checked={statement.intruder.hasRadio}
                               onChange={this.props.onChange}
                               title='Has the vehicle radio installed and operational?'/>
                    </div>
                </div>
                <div className='form-group'>
                    <Input type='textarea' label='What was the vehicle doing on the Runway?' name='intruderAction'
                           value={statement.intruder.intruderAction} onChange={this.props.onChange}
                           title='What was the vehicle doing on the Runway?'/>
                </div>
            </Panel>
        );
    }
});

module.exports = VehicleIntruder;
