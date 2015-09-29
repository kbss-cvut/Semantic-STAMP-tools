/**
 * @jsx
 */

'use strict;'

var React = require('react');
var Button = require('react-bootstrap').Button;
var Panel = require('react-bootstrap').Panel;
var CollapsibleMixin = require('react-bootstrap').CollapsibleMixin;
var classNames = require('classnames');

var Input = require('../../Input');
var EccairsLink = require('../../EccairsLink');

var AircraftRegistration = React.createClass({
    mixins: [CollapsibleMixin],

    getCollapsibleDOMNode: function () {
        return React.findDOMNode(this.refs.panel);
    },
    getCollapsibleDimensionValue: function () {
        return React.findDOMNode(this.refs.panel).scrollHeight;
    },
    onToggleCollapsible: function (e) {
        e.preventDefault();
        this.setState({expanded: !this.state.expanded});
    },


    render: function () {
        var styles = this.getCollapsibleClassSet();
        var registrationLabel = (<span>Aircraft Registration (<EccairsLink text='244'/>)</span>);
        var stateLabel = (<span>State of Registry (<EccairsLink text='281'/>)</span>);
        return (
            <div>
                <div className='form-group'>
                    <Button bsStyle='link' onClick={this.onToggleCollapsible}>Aircraft Registration</Button>
                </div>
                <Panel ref='panel' className={classNames(styles)}>
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
            </div>
        );
    }
});

module.exports = AircraftRegistration;
