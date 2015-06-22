/**
 * Created by kidney on 6/22/15.
 */

'use strict';

var React = require('react');
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;

var PersonIntruder = React.createClass({
    render: function () {
        var statement = this.props.statement;
        return (
            <Panel header='Person Event' style={{margin: '15px 0px 0px 0px'}}>
                <div className='form-group float-container'>
                    <div className='report-detail-float'>
                        <Input type='text' label='Personnel Category' name='personCategory'
                               value={statement.intruder.personCategory}
                               onChange={this.props.onChange} title='Personnel on the ground category'/>
                    </div>
                    <div className='report-detail-float-right'>
                        <Input type='text' label='Organization/Aerodrome Department' name='personOrganization'
                               value={statement.intruder.personOrganization} onChange={this.props.onChange}
                               title='Person Organization/Aerodrome Department'/>
                    </div>
                </div>
                <div className='form-group'>
                    <Input type='textarea' label='What was the person doing on the Runway?' name='intruderAction'
                           value={statement.intruder.intruderAction} onChange={this.props.onChange}
                           title='What was the person doing on the Runway?'/>
                </div>
            </Panel>
        );
    }
});

module.exports = PersonIntruder;
