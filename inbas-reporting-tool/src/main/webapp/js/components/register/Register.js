/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Button = require('react-bootstrap').Button;
var Alert = require('react-bootstrap').Alert;

var LinkedStateMixin = require('react-addons-linked-state-mixin');
var injectIntl = require('react-intl').injectIntl;

var Input = require('../Input');
var Mask = require('../Mask');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var Ajax = require('../../utils/Ajax');
var Actions = require('../../actions/Actions');
var I18nMixin = require('../../i18n/I18nMixin');

var title = (<h3>INBAS Reporting Tool - Registration</h3>);

var Register = React.createClass({
    mixins: [LinkedStateMixin, I18nMixin],
    getInitialState: function () {
        return {
            firstName: '',
            lastName: '',
            username: '',
            password: '',
            passwordConfirm: '',
            passwordMatch: true,
            alertVisible: false,
            errorMessage: '',
            mask: false
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

    onKeyDown: function (e) {
        if (e.key === 'Enter') {
            this.register();
        }
    },

    dismissAlert: function () {
        this.setState({alertVisible: false});
    },

    isValid: function () {
        var state = this.state;
        return (state.firstName !== '' && state.lastName !== '' && state.username !== '' && state.password !== '' && state.passwordMatch);
    },

    register: function () {
        if (!this.isValid()) {
            return;
        }
        var data = {
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            username: this.state.username,
            password: this.state.password
        };
        Ajax.post('rest/persons', data).end(function (err, resp) {
            if (err) {
                this.setState({
                    alertVisible: true,
                    mask: false,
                    errorMessage: resp.body.message ? resp.body.message : 'Unknown error.'
                });
            }
            if (resp.status === 201) {
                this.doSyntheticLogin(data.username, data.password);
            }
        }.bind(this));
        this.setState({mask: true});
    },

    /**
     * After successful registration, perform a synthetic login so that the user receives his session and can start
     * working.
     */
    doSyntheticLogin: function (username, password) {
        Ajax.post('j_spring_security_check', null, 'form').send('username=' + username).send('password=' + password)
            .end(function (err, resp) {
                if (err) {
                    console.log('Unable to perform synthetic login. Received response with status ' + err.status);
                }
                var status = JSON.parse(resp.text);
                if (!status.success || !status.loggedIn) {
                    this.setState({alertVisible: true});
                    return;
                }
                Actions.loadUser();
                Routing.transitionToHome();
            }.bind(this));
    },

    cancel: function () {
        Routing.transitionTo(Routes.login);
    },

    render: function () {
        var panelCls = this.state.alertVisible ? 'register-panel expanded' : 'register-panel';
        var mask = this.state.mask ? (<Mask text={this.i18n('register.mask')}/>) : null;
        return (
            <Panel header={<h3>{this.i18n('register.title')}</h3>} bsStyle='info' className={panelCls}>
                {mask}
                <form className='form-horizontal' style={{margin: '0.5em 0 0 0'}}>
                    {this.renderAlert()}
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='text' name='firstName' label={this.i18n('register.first-name')}
                                   labelClassName='col-xs-4'
                                   valueLink={this.linkState('firstName')} wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='col-xs-6'>
                            <Input type='text' name='lastName' label={this.i18n('register.last-name')}
                                   labelClassName='col-xs-4'
                                   valueLink={this.linkState('lastName')} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='text' name='username' label={this.i18n('register.username')}
                                   labelClassName='col-xs-4'
                                   valueLink={this.linkState('username')} wrapperClassName='col-xs-8'/>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-xs-6'>
                            <Input type='password' name='password' label={this.i18n('register.password')}
                                   labelClassName='col-xs-4'
                                   onChange={this.onPasswordChange} value={this.state.password}
                                   wrapperClassName='col-xs-8'/>
                        </div>
                        <div className='col-xs-6'>
                            {this.renderPasswordConfirm()}
                        </div>
                    </div>
                    <div style={{margin: '1em 0em 0em 0em', textAlign: 'center'}}>
                        <Button bsStyle='success' bsSize='small' ref='submit'
                                disabled={!this.isValid() || this.state.mask}
                                onClick={this.register}>{this.i18n('register.submit')}</Button>
                        <Button bsSize='small' onClick={this.cancel} style={{margin: '0 0 0 3.2em'}}
                                disabled={this.state.mask}>{this.i18n('cancel')}</Button>
                    </div>
                </form>
            </Panel>
        );
    },

    renderAlert: function () {
        return this.state.alertVisible ? (
            <Alert bsStyle='danger' bsSize='small' dismissAfter={3000} onDismiss={this.dismissAlert}>
                <div>{this.state.errorMessage}</div>
            </Alert>
        ) : null;
    },

    renderPasswordConfirm: function () {
        if (this.state.passwordMatch) {
            return (
                <Input type='password' name='passwordConfirm' label={this.i18n('register.password-confirm')}
                       labelClassName='col-xs-4'
                       wrapperClassName='col-xs-8' onChange={this.onPasswordChange} onKeyDown={this.onKeyDown}
                       value={this.state.passwordConfirm}/>);
        } else {
            return (
                <Input type='password' name='passwordConfirm' label={this.i18n('register.password-confirm')}
                       labelClassName='col-xs-4'
                       wrapperClassName='col-xs-8' onChange={this.onPasswordChange} onKeyDown={this.onKeyDown}
                       value={this.state.passwordConfirm} bsStyle='error'
                       title={this.i18n('register.passwords-not-matching-tooltip')} hasFeedback/>);
        }
    }
});

module.exports = injectIntl(Register);
