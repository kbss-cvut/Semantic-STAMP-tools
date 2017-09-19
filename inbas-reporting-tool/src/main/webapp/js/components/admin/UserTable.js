import React from "react";
import PropTypes from "prop-types";
import {Button, Glyphicon, Table} from "react-bootstrap";
import classNames from "classnames";

import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import Mask from "../Mask";
import Vocabulary from "../../constants/Vocabulary";

class UserTable extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    render() {
        return <Table bordered condensed hover striped>
            <thead>
            <tr>
                <th className='col-xs-5 content-center'>{this.i18n('users.table.name')}</th>
                <th className='col-xs-4 content-center'>{this.i18n('users.table.username')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('users.table.status')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('table-actions')}</th>
            </tr>
            </thead>
            <tbody>
            {this._renderRows()}
            </tbody>
        </Table>;
    }

    _renderRows() {
        const users = this.props.users,
            pendingUnlockUser = this.props.pendingUnlockUser,
            actions = this.props.actions,
            rows = [];
        for (let i = 0, len = users.length; i < len; i++) {
            rows.push(<UserRow key={users[i].uri} user={users[i]} unlock={actions.unlock} disable={actions.disable}
                               enable={actions.enable} pending={pendingUnlockUser === users[i]}/>);
        }
        return rows;
    }
}

UserTable.propTypes = {
    users: PropTypes.array.isRequired,
    actions: PropTypes.object.isRequired,
    pendingUnlockUser: PropTypes.object
};

let UserRow = (props) => {
    const user = props.user,
        i18n = props.i18n,
        isLocked = isUserLocked(user),
        isDisabled = isUserDisabled(user),
        classes = classNames('vertical-middle', {'italics': isDisabled}),
        status = props.pending ? <Mask classes='mask-container' withoutText/> :
            <Glyphicon glyph={isLocked ? 'alert' : 'ok'}
                       title={i18n(isLocked ? 'users.table.locked.tooltip' : 'users.table.not.locked.tooltip')}/>;
    let changeButton;
    if (isDisabled) {
        changeButton = <Button bsStyle='primary' bsSize='small' onClick={props.enable}>{i18n('users.enable')}</Button>;
    } else {
        changeButton =
            <Button bsStyle='primary' bsSize='small' onClick={props.disable}>{i18n('users.disable')}</Button>;
    }

    return <tr>
        <td className={classes}>{user.firstName + ' ' + user.lastName}</td>
        <td className={classes}>{user.username}</td>
        <td className='vertical-middle content-center status' style={{position: 'relative'}}>
            {status}
        </td>
        <td className='vertical-middle actions'>
            {isLocked && !props.pending && <Button bsStyle='primary' bsSize='small' onClick={() => props.unlock(user)}
                                                   title={i18n('users.table.locked.unlock.tooltip')}>{i18n('users.table.locked.unlock')}</Button>}
            {changeButton}
        </td>
    </tr>;
};

UserRow.propTypes = {
    user: PropTypes.object.isRequired,
    unlock: PropTypes.func.isRequired,
    enable: PropTypes.func.isRequired,
    disable: PropTypes.func.isRequired,
    pending: PropTypes.bool
};

UserRow = injectIntl(I18nWrapper(UserRow));

function isUserLocked(user) {
    return user.types.indexOf(Vocabulary.LOCKED) !== -1;
}

function isUserDisabled(user) {
    return user.types.indexOf(Vocabulary.DISABLED) !== -1;
}

export default injectIntl(I18nWrapper(UserTable));
