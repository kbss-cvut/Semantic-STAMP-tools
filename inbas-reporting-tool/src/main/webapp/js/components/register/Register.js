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
            alertVisible: false
        }
    },

    onChange: function (e) {
    },

    register: function () {
    },

    cancel: function () {
    },

    render: function () {
        return (
            <Panel header={title} bsStyle='info' className='register-panel'>
                <form className='form-horizontal'>
                    {this.renderAlert()}
                    <div className='float-container'>
                        <div className='component-float-left'>
                            <Input type='text' name='firstName' label='First name' labelClassName='col-xs-4'
                                   value={this.state.firstName} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='component-float-right'>
                            <Input type='text' name='lastName' label='Last name' labelClassName='col-xs-4'
                                   value={this.state.lastName} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container'>
                        <div className='component-float-left'>
                            <Input type='text' name='username' label='Username' labelClassName='col-xs-4'
                                   value={this.state.username} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container'>
                        <div className='component-float-left'>
                            <Input type='text' name='password' label='Password' labelClassName='col-xs-4'
                                   value={this.state.password} onChange={this.onChange} wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='component-float-right'>
                            <Input type='text' name='passwordConfirm' label='Password (confirm)'
                                   labelClassName='col-xs-4'
                                   value={this.state.passwordConfirm} onChange={this.onChange}
                                   wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='float-container'>
                        <div className='component-float-left'>
                            <div className='col-xs-4'>&nbsp;</div>
                            <div className='col-xs-8'>
                                <Button bsStyle='success' bsSize='small' onClick={this.register}>Register</Button>
                            </div>
                        </div>
                        <div className='compnent-float-right'>
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
    }
});

module.exports = Register;
