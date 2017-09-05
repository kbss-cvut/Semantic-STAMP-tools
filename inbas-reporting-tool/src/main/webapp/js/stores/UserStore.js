'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');

let currentUser = null;
let loaded = false;

function loadCurrentUser() {
    Ajax.get('rest/persons/current').end(UserStore.userLoaded);
}

const UserStore = Reflux.createStore({
    listenables: [Actions],

    onLoadUsers: function () {
        Ajax.get('rest/persons').end((data) => {
            this.trigger({
                action: Actions.loadUsers,
                users: data
            });
        }, () => {
            this.trigger({
                action: Actions.loadUsers,
                users: []
            });
        });
    },

    onLoadUser: function () {
        loadCurrentUser();
    },

    userLoaded: function (user) {
        currentUser = user;
        loaded = true;
        this.trigger({
            action: Actions.loadUser,
            user: this.getCurrentUser()
        });
    },

    getCurrentUser: function () {
        return currentUser;
    },

    isLoaded: function () {
        return loaded;
    },

    onUpdateUser: function (user, onSuccess, onError) {
        Ajax.put('rest/persons/current', user).end(onSuccess, onError);
    }
});

module.exports = UserStore;
