import React from "react";
import {Alert, Button, Form, Panel} from "react-bootstrap";
import assign from "object-assign";

import Authentication from "../../utils/Authentication";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Input from "../HorizontalInput";
import Mask from "../Mask";
import Routes from "../../utils/Routes";
import Routing from "../../utils/Routing";

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            username: '',
            password: '',
            alertVisible: false,
            mask: false
        };
    }

    componentDidMount() {
        this.usernameField.focus();
    }

    onChange = (e) => {
        const state = assign({}, this.state);
        state[e.target.name] = e.target.value;
        state.alertVisible = false;
        this.setState(state);
    };

    onKeyPress = (e) => {
        if (e.key === 'Enter') {
            this.login();
        }
    };

    onLoginError = () => {
        this.setState({alertVisible: true, mask: false});
    };

    login() {
        Authentication.login(this.state.username, this.state.password, this.onLoginError);
        this.setState({mask: true});
    }

    register() {
        Routing.transitionTo(Routes.register);
    }


    render() {
        const panelCls = this.state.alertVisible ? 'login-panel expanded' : 'login-panel',
            mask = this.state.mask ? <Mask text={this.i18n('login.progress-mask')}/> : null;
        return <Panel header={<h3>{this.i18n('login.title')}</h3>} bsStyle='info' className={panelCls}>
            {mask}
            <Form horizontal>
                {this._renderAlert()}
                <Input type='text' name='username' ref={c => this.usernameField = c} label={this.i18n('login.username')}
                       value={this.state.username}
                       onChange={this.onChange} labelWidth={3} onKeyPress={this.onKeyPress}
                       inputWidth={9}/>
                <Input type='password' name='password' label={this.i18n('login.password')}
                       value={this.state.password}
                       onChange={this.onChange} labelWidth={3} onKeyPress={this.onKeyPress}
                       inputWidth={9}/>

                <div className='col-xs-3'>&nbsp;</div>
                <div className='col-xs-9' style={{padding: '0 0 0 7px'}}>
                    <Button bsStyle='success' bsSize='small' onClick={this.login}
                            disabled={this.state.mask}>{this.i18n('login.submit')}</Button>
                    <Button bsStyle='link' bsSize='small' onClick={this.register} style={{padding: '0 0 0 15px'}}
                            disabled={this.state.mask}>{this.i18n('login.register')}</Button>
                </div>
            </Form>
        </Panel>;
    }

    _renderAlert() {
        return this.state.alertVisible ? <Alert bsStyle='danger' bsSize='small'>
            <div>{this.i18n('login.error')}</div>
        </Alert> : null;
    }
}

export default injectIntl(I18nWrapper(Login));
