import React from "react";

import Actions from "../../actions/Actions";
import Users from "./Users";
import UserStore from "../../stores/UserStore";

export default class UsersController extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            users: null
        };
    }

    componentDidMount() {
        this.unsubscribe = UserStore.listen(this._onUsersLoaded);
        Actions.loadUsers();
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onUsersLoaded = (data) => {
        if (data.action === Actions.loadUsers) {
            this.setState({users: data.users});
        }
    };

    _unlock = (user, newPassword, onSuccess, onError) => {
        Actions.unlockUser(user, newPassword, () => {
            Actions.loadUsers();
            onSuccess();
        }, onError);
    };

    _disable = (user) => {

    };

    _enable = (user) => {

    };

    render() {
        const actions = {
            unlock: this._unlock,
            disable: this._disable,
            enable: this._enable
        };
        return <div>
            <Users users={this.state.users} actions={actions}/>
        </div>;
    }
}
