/**
 * Created by kidney on 6/22/15.
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;

var Input = require('../../../../Input');

var PersonIntruder = React.createClass({
    render: function () {
        var statement = this.props.statement;
        return (
            <Panel header='Person Event' style={{margin: '15px 0px 0px 0px'}}>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='text' label='Personnel Category' name='category'
                               value={statement.intruder.category}
                               onChange={this.props.onChange} title='Personnel on the ground category'/>
                    </div>
                    <div className='col-xs-6'>
                        <Input type='text' label='Organization/Aerodrome Department' name='organization'
                               value={statement.intruder.organization} onChange={this.props.onChange}
                               title='Person Organization/Aerodrome Department'/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-12'>
                        <Input type='textarea' label='What was the person doing on the Runway?' name='wasDoing'
                               value={statement.intruder.wasDoing} onChange={this.props.onChange}
                               title='What was the person doing on the Runway?'/>
                    </div>
                </div>
            </Panel>
        );
    }
});

module.exports = PersonIntruder;
