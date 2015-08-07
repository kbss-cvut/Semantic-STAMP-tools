/**
 * @author kidney
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Button = require('react-bootstrap').Button;

var Input = require('../Input');

var title = (<h3>INBAS Reporting Tool - Registration</h3>);

var Register = React.createClass({
    getInitialState: function () {
        return {
            firstName: '',
            lastName: '',
            username: '',
            password: '',
            passwordConfirm: '',
            passwordConfirmMatches: true,
            alertVisible: false
        }
    },

    onChange: function (e) {
        var state = this.state;
        state[e.target.name] = e.target.value;
        state.alertVisible = false;
        this.setState(state);
    },

    checkPasswordConfirm: function (e) {
        var val = e.target.value;
        if (val !== this.state.password) {
            this.setState({passwordConfirm: val, passwordConfirmMatches: false});
        } else {
            this.setState({passwordConfirm: val, passwordConfirmMatches: true});
        }
    },

    register: function () {
    },

    cancel: function () {
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
                                   value={this.state.firstName} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='component-float-right'>
                            <Input type='text' name='lastName' label='Last name' labelClassName='col-xs-4'
                                   value={this.state.lastName} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container' style={containerStyle}>
                        <div className='component-float-left'>
                            <Input type='text' name='username' label='Username' labelClassName='col-xs-4'
                                   value={this.state.username} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container' style={containerStyle}>
                        <div className='component-float-left'>
                            <Input type='text' name='password' label='Password' labelClassName='col-xs-4'
                                   value={this.state.password} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='component-float-right'>
                            {this.renderPasswordConfirm()}
                        </div>
                    </div>
                    <div className='float-container' style={containerStyle}>
                        <div className='component-float-left'>
                            <div className='col-xs-4'>&nbsp;</div>
                            <div className='col-xs-8'>
                                <Button bsStyle='success' bsSize='small' ref='submit'
                                        onClick={this.register}>Register</Button>
                            </div>
                        </div>
                        <div className='component-float-right'>
                            <Button bsStyle='default' onClick={this.cancel}>Cancel</Button>
                        </div>
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
        if (this.state.passwordConfirmMatches) {
            return (<Input type='text' name='passwordConfirm' label='Password (confirm)'
                           labelClassName='col-xs-4'
                           value={this.state.passwordConfirm} onChange={this.onChange}
                           wrapperClassName='col-xs-8'/>);
        } else {
            return (<Input type='text' name='passwordConfirm' label='Password (confirm)'
                           labelClassName='col-xs-4'
                           value={this.state.passwordConfirm} onChange={this.onChange}
                           wrapperClassName='col-xs-8' bsStyle='error' hasFeedback/>);
        }
    }
});

module.exports = Register;
