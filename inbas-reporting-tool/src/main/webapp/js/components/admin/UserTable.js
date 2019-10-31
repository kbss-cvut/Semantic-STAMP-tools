import React from "react";
import PropTypes from "prop-types";
import {Table} from "react-bootstrap";

import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import UserRow from "./UserRow";

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
            statusPending = this.props.statusPending,
            actions = this.props.actions,
            rows = [];
        for (let i = 0, len = users.length; i < len; i++) {
            rows.push(<UserRow key={users[i].uri} user={users[i]} unlock={actions.unlock} disable={actions.disable}
                               enable={actions.enable} pending={statusPending === users[i]}/>);
        }
        return rows;
    }
}

UserTable.propTypes = {
    users: PropTypes.array.isRequired,
    actions: PropTypes.object.isRequired,
    statusPending: PropTypes.object
};

export default injectIntl(I18nWrapper(UserTable));
