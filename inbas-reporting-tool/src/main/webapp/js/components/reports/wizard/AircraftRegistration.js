/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Panel = require('react-bootstrap').Panel;
var Fade = require('react-bootstrap').Fade;

var Input = require('../../Input');
var EccairsLink = require('../../EccairsLink');

var AircraftRegistration = React.createClass({

    getInitialState: function () {
        return {
            expanded: false
        };
    },

    onToggleCollapsible: function (e) {
        e.preventDefault();
        this.setState({expanded: !this.state.expanded});
    },


    render: function () {
        var registrationLabel = (<span>Aircraft Registration (<EccairsLink text='244'/>)</span>);
        var stateLabel = (<span>State of Registry (<EccairsLink text='281'/>)</span>);
        return (
            <div>
                <div className='form-group'>
                    <Button bsStyle='link' onClick={this.onToggleCollapsible}>Aircraft Registration</Button>
                </div>
                <Fade in={this.state.expanded} unmountOnExit={true} timeout={100}>
                    <Panel ref='panel'>
                        <div className='row'>
                            <div className='col-xs-6'>
                                <Input type='text' label={registrationLabel} name='registration'
                                       value={this.props.registration} onChange={this.props.onChange}
                                       title='Aircraft registration'/>
                            </div>
                            <div className='col-xs-6'>
                                <Input type='text' label={stateLabel} name='stateOfRegistry'
                                       value={this.props.stateOfRegistry} onChange={this.props.onChange}
                                       title='Aircraft state of registry'/>
                            </div>
                        </div>
                    </Panel>
                </Fade>
            </div>
        );
    }
});

module.exports = AircraftRegistration;
