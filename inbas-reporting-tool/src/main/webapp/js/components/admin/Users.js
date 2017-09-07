import React from "react";
import PropTypes from "prop-types";
import {Button, Panel} from "react-bootstrap";

import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Mask from "../Mask";
import MessageWrapper from "../misc/hoc/MessageWrapper";
import UserRegistration from "./UserRegistration";
import UserTable from "./UserTable";

class Users extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showRegistration: false
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

    render() {
        const users = this.props.users;
        if (users === null) {
            return <Mask/>;
        }
        return <Panel header={<h5>{this.i18n('users.title')}</h5>} bsStyle='primary'>
            <UserRegistration onClose={this._closeRegistration} onSuccess={this._finishRegistration}
                              show={this.state.showRegistration}/>
            <UserTable users={users}/>
            <Button bsSize='small' bsStyle='primary'
                    onClick={this._openRegistration}>{this.i18n('users.register')}</Button>
        </Panel>;
    }
}

Users.propTypes = {
    users: PropTypes.array
};

export default injectIntl(I18nWrapper(MessageWrapper(Users)));
