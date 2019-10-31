import React from "react";
import PropTypes from "prop-types";
import {Button, Modal, Panel} from "react-bootstrap";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Input from "../Input";

class PasswordReset extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            password: '',
            passwordConfirm: ''
        };
    }

    componentWillReceiveProps() {
        this.setState({password: '', passwordConfirm: ''});
    }

    componentDidUpdate(prevProps) {
        if (this.props.show && !prevProps.show) {
            this.passwordInput.focus();
        }
    }

    _onChange = (e) => {
        const change = {};
        change[e.target.name] = e.target.value;
        this.setState(change);
    };

    _onSubmit = () => {
        if (!this._passwordValid()) {
            return;
        }
        this.props.onSubmit(this.state.password);
    };

    _onCancel = () => {
        this.setState({password: '', passwordConfirm: ''});
        this.props.onCancel();
    };

    _passwordValid() {
        return this.state.password.length > 0 && this.state.password === this.state.passwordConfirm;
    }

    render() {
        if (!this.props.show) {
            return null;
        }
        const user = this.props.user,
            passMatch = this.state.password === this.state.passwordConfirm,
            submitDisabled = !this._passwordValid();
        return <Modal show={this.props.show} onHide={this.props.onCancel}>
            <Panel header={<h3>{this.props.formatMessage('users.unlock.title', {
                firstName: user.firstName,
                lastName: user.lastName
            })}</h3>} bsStyle='info' className='profile-panel'>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input name='password' type='password' ref={c => this.passwordInput = c}
                               label={this.i18n('profile.password.new')} value={this.state.password}
                               onChange={this._onChange}/>
                    </div>
                    <div className='col-xs-6'>
                        <Input name='passwordConfirm' type='password' label={this.i18n('profile.password.confirm')}
                               validation={passMatch ? undefined : 'error'}
                               title={passMatch ? undefined : this.i18n('register.passwords-not-matching-tooltip')}
                               value={this.state.passwordConfirm} onChange={this._onChange}/>
                    </div>
                </div>
                <div className='row content-center'>
                    <Button bsStyle='success' bsSize='small' disabled={submitDisabled} onClick={this._onSubmit}
                            title={submitDisabled ? this.i18n('users.unlock.submit.tooltip') : undefined}>
                        {this.i18n('users.table.locked.unlock')}
                    </Button>
                    <Button bsSize='small' className='right' onClick={this._onCancel}>{this.i18n('cancel')}</Button>
                </div>
            </Panel>
        </Modal>;
    }
}

PasswordReset.propTypes = {
    show: PropTypes.bool,
    user: PropTypes.object,
    onSubmit: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired
};

PasswordReset.defaultProps = {
    show: false
};

export default injectIntl(I18nWrapper(PasswordReset));
