/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Input = require('react-bootstrap').Input;
var Button = require('react-bootstrap').Button;
var Alert = require('react-bootstrap').Alert;

var title = (<h3>INBAS Reporting Tool - Login</h3>);

var Login = React.createClass({

    getInitialState: function () {
        return {
            username: '',
            password: '',
            alertVisible: false
        }
    },

    onChange: function (e) {
        var state = this.state;
        state[e.target.name] = e.target.value;
        state.alertVisible = false;
        this.setState(state);
    },

    login: function () {
        // TODO Submit form
    },

    register: function () {

    },


    render: function () {
        return (
            <Panel header={title} bsStyle='info' className='login-panel'>
                <form className='form-horizontal'>
                    {this.renderAlert()}
                    <Input type='text' name='username' label='Username' value={this.state.username}
                           onChange={this.onChange} labelClassName='col-xs-3' wrapperClassName='col-xs-9'/>
                    <Input type='text' name='password' label='Password' value={this.state.password}
                           onChange={this.onChange} labelClassName='col-xs-3' wrapperClassName='col-xs-9'/>

                    <div className='col-xs-3'>&nbsp;</div>
                    <div className='col-xs-9' style={{padding: '0 0 0 7px'}}>
                        <Button bsStyle='success' bsSize='small' onClick={this.login}>Login</Button>
                        <Button bsStyle='link' bsSize='small' onClick={this.register}>Register</Button>
                    </div>
                </form>
            </Panel>
        )
    },

    renderAlert: function () {
        return this.state.alertVisible ? (
            <Alert>
                <div>Authentication failed</div>
            </Alert>
        ) : null;
    }
});

module.exports = Login;
