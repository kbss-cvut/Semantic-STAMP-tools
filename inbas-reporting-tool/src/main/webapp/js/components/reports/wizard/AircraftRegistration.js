/**
 * Created by ledvima1 on 22.6.15.
 */

'use strict;'

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;
var CollapsibleMixin = require('react-bootstrap').CollapsibleMixin;
var classNames = require('classnames');

var AircraftRegistration = React.createClass({
    mixins: [CollapsibleMixin],

    getCollapsibleDOMNode(){
        return React.findDOMNode(this.refs.panel);
    },
    getCollapsibleDimensionValue(){
        return React.findDOMNode(this.refs.panel).scrollHeight;
    },
    onToggleCollapsible: function (e) {
        e.preventDefault();
        this.setState({expanded: !this.state.expanded});
    },


    render: function () {
        var styles = this.getCollapsibleClassSet();
        return (
            <div>
                <div className='form-group'>
                    <Button bsStyle='link' onClick={this.onToggleCollapsible}>Aircraft Registration</Button>
                </div>
                <Panel ref='panel' className={classNames(styles)}>
                    <div style={{overflow: 'hidden'}}>
                        <div className='report-detail-float'>
                            <Input type='text' label='Aircraft Registration' name='registration'
                                   value={this.props.registration} onChange={this.props.onChange}
                                   title='Aircraft registration'/>
                        </div>
                        <div className='report-detail-float-right'>
                            <Input type='text' label='State of Registry' name='stateOfRegistry'
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
