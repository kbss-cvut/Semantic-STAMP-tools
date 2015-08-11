/**
 * @author kidney
 */

'use strict';

var React = require('react/addons');
var Panel = require('react-bootstrap').Panel;
var Button = require('react-bootstrap').Button;

var Input = require('../Input');
var router = require('../../utils/router');

var title = (<h3>INBAS Reporting Tool - Registration</h3>);

var Register = React.createClass({
    mixins: [React.addons.LinkedStateMixin],
    getInitialState: function () {
        return {
            firstName: '',
            lastName: '',
            username: '',
            password: '',
            passwordConfirm: '',
            passwordMatch: true,
            alertVisible: false
        }
    },

    onPasswordChange: function (e) {
        this.state[e.target.name] = e.target.value;
        if (this.state.password !== this.state.passwordConfirm) {
            this.setState({passwordMatch: false});
        } else {
            this.setState({passwordMatch: true});
        }
    },

    isValid: function () {
        var state = this.state;
        return (state.firstName !== '' && state.lastName !== '' && state.username !== '' && state.password !== '' && state.passwordMatch);
    },

    register: function () {
    },

    cancel: function () {
        router.transitionTo('login');
    },

    render: function () {
        var containerStyle = {margin: '1em 0em 0em 0em'};
        return (
            <Panel header={title} bsStyle='info' className='register-panel'>
                <form className='form-horizontal'>
                    {this.renderAlert()}
                    <div className='float-container' style={containerStyle}>
                        <div className='component-float-left'>
                            <Input type='text' name='firstName' label='First name' labelClassName='col-xs-4'
                                   valueLink={this.linkState('firstName')} wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='component-float-right'>
                            <Input type='text' name='lastName' label='Last name' labelClassName='col-xs-4'
                                   valueLink={this.linkState('lastName')} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container' style={containerStyle}>
                        <div className='component-float-left'>
                            <Input type='text' name='username' label='Username' labelClassName='col-xs-4'
                                   valueLink={this.linkState('username')} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container' style={containerStyle}>
                        <div className='component-float-left'>
                            <Input type='password' name='password' label='Password' labelClassName='col-xs-4'
                                   onChange={this.onPasswordChange} value={this.state.password}
                                   wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='component-float-right'>
                            {this.renderPasswordConfirm()}
                        </div>
                    </div>
                    <div style={{margin: '1em 0em 0em 0em', textAlign: 'center'}}>
                        <Button bsStyle='success' bsSize='small' ref='submit'
                                disabled={!this.isValid()} onClick={this.register}>Register</Button>
                        <Button bsSize='small' onClick={this.cancel} style={{margin: '0 0 0 4.7em'}}>Cancel</Button>
                    </div>
                </form>
            </Panel>

        );
    },

    renderAlert: function () {
        return this.state.alertVisible ? (
            <Alert>
                <div>An error occurred during registration.</div>
            </Alert>
        ) : null;
    },

    renderPasswordConfirm: function () {
        if (this.state.passwordMatch) {
            return (<Input type='password' name='passwordConfirm' label='Password (confirm)' labelClassName='col-xs-4'
                           wrapperClassName='col-xs-8' onChange={this.onPasswordChange}
                           value={this.state.passwordConfirm}/>);
        } else {
            return (<Input type='password' name='passwordConfirm' label='Password (confirm)' labelClassName='col-xs-4'
                           wrapperClassName='col-xs-8' onChange={this.onPasswordChange}
                           value={this.state.passwordConfirm} bsStyle='error'
                           hasFeedback/>);
        }
    }
});

module.exports = Register;
