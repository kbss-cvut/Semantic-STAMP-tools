/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Button = require('react-bootstrap').Button;
var Alert = require('react-bootstrap').Alert;

var Input = require('../Input');
var router = require('../../utils/router');
var Ajax = require('../../utils/Ajax');

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

    onKeyPress: function (e) {
        if (e.key === 'Enter') {
            this.login();
        }
    },

    login: function () {
        Ajax.post('j_spring_security_check', null, 'form')
            .send('username=' + this.state.username).send('password=' + this.state.password)
            .end(function (err, resp) {
                if (err) {
                    this.setState({alertVisible: true});
                    return;
                }
                var status = JSON.parse(resp.text);
                if (!status.success || !status.loggedIn) {
                    this.setState({alertVisible: true});
                    return;
                }
                console.log('User successfully authenticated.');
                router.transitionToOriginalTarget();
            }.bind(this));
    },

    register: function () {
        router.transitionTo('register');
    },


    render: function () {
        var panelCls = this.state.alertVisible ? 'login-panel expanded' : 'login-panel';
        return (
            <Panel header={title} bsStyle='info' className={panelCls}>
                <form className='form-horizontal'>
                    {this.renderAlert()}
                    <Input type='text' name='username' label='Username' value={this.state.username}
                           onChange={this.onChange} labelClassName='col-xs-3' wrapperClassName='col-xs-9'/>
                    <Input type='password' name='password' label='Password' value={this.state.password}
                           onChange={this.onChange} labelClassName='col-xs-3' onKeyPress={this.onKeyPress}
                           wrapperClassName='col-xs-9'/>

                    <div className='col-xs-3'>&nbsp;</div>
                    <div className='col-xs-9' style={{padding: '0 0 0 7px'}}>
                        <Button bsStyle='success' bsSize='small' onClick={this.login}>Login</Button>
                        <Button bsStyle='link' bsSize='small' onClick={this.register} style={{padding: '0 0 0 15px'}}>Register</Button>
                    </div>
                </form>
            </Panel>
        )
    },

    renderAlert: function () {
        return this.state.alertVisible ? (
            <Alert bsStyle='danger' bsSize='small'>
                <div>Authentication failed.</div>
            </Alert>
        ) : null;
    }
});

module.exports = Login;
