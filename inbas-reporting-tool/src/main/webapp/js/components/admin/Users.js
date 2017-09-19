import React from "react";
import PropTypes from "prop-types";
import {Button, Panel} from "react-bootstrap";

import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Mask from "../Mask";
import MessageWrapper from "../misc/hoc/MessageWrapper";
import PasswordReset from "./PasswordReset";
import UserRegistration from "./UserRegistration";
import UserTable from "./UserTable";

class Users extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showRegistration: false,
            showUnlock: false,
            unlockUser: null
        };
    }

    _openRegistration = () => {
        this.setState({showRegistration: true});
    };

    _finishRegistration = () => {
        this.props.showSuccessMessage(this.i18n('users.register.success'));
        this._closeRegistration();
    };

    _closeRegistration = () => {
        this.setState({showRegistration: false});
    };

    _openUnlock = (user) => {
        this.setState({showUnlock: true, unlockUser: user});
    };

    _unlockUser = (newPassword) => {
        // TODO
    };

    _closeUnlock = () => {
        this.setState({showUnlock: false, unlockUser: null});
    };

    render() {
        const users = this.props.users;
        if (users === null) {
            return <Mask/>;
        }
        const actions = {
            unlock: this._openUnlock
        };
        return <Panel header={<h5>{this.i18n('users.title')}</h5>} bsStyle='primary'>
            <UserRegistration onClose={this._closeRegistration} onSuccess={this._finishRegistration}
                              show={this.state.showRegistration}/>
            <PasswordReset show={this.state.showUnlock} user={this.state.unlockUser} onSubmit={this._unlockUser}
                           onCancel={this._closeUnlock}/>
            <UserTable users={users} actions={actions}/>
            <Button bsSize='small' bsStyle='primary'
                    onClick={this._openRegistration}>{this.i18n('users.register')}</Button>
        </Panel>;
    }
}

Users.propTypes = {
    users: PropTypes.array,
    actions: PropTypes.object.isRequired
};

export default injectIntl(I18nWrapper(MessageWrapper(Users)));
