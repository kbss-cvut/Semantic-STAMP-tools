import React from "react";
import PropTypes from "prop-types";
import {Panel} from "react-bootstrap";

import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Mask from "../Mask";

class Users extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    render() {
        const users = this.props.users;
        if (users === null) {
            return <Mask/>;
        }
        return <Panel header={<h5>{this.i18n('users.title')}</h5>} bsStyle='info'></Panel>;
    }
}

Users.propTypes = {
    users: PropTypes.array
};

export default injectIntl(I18nWrapper(Users));
