import React from "react";
import PropTypes from "prop-types";
import {Button, Glyphicon, Table} from "react-bootstrap";

import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
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
            rows = [];
        for (let i = 0, len = users.length; i < len; i++) {
            rows.push(<UserRow key={users[i].uri} user={users[i]} unlock={this.props.actions.unlock}/>);
        }
        return rows;
    }
}

UserTable.propTypes = {
    users: PropTypes.array.isRequired,
    actions: PropTypes.object.isRequired
};

let UserRow = (props) => {
    const user = props.user,
        i18n = props.i18n,
        isLocked = isUserLocked(user);
    return <tr>
        <td className='vertical-middle'>{user.firstName + ' ' + user.lastName}</td>
        <td className='vertical-middle'>{user.username}</td>
        <td className='vertical-middle content-center status'>
            <Glyphicon glyph={isLocked ? 'alert' : 'ok'}
                       title={i18n(isLocked ? 'users.table.locked.tooltip' : 'users.table.not.locked.tooltip')}/>
        </td>
        <td className='vertical-middle actions'>
            {isLocked && <Button bsStyle='primary' bsSize='small' onClick={() => props.unlock(user)}
                                 title={i18n('users.table.locked.unlock.tooltip')}>{i18n('users.table.locked.unlock')}</Button>}
        </td>
    </tr>;
};

UserRow.propTypes = {
    user: PropTypes.object.isRequired,
    unlock: PropTypes.func.isRequired
};

UserRow = injectIntl(I18nWrapper(UserRow));

function isUserLocked(user) {
    return user.types.indexOf(Vocabulary.LOCKED) !== -1;
}

export default injectIntl(I18nWrapper(UserTable));
