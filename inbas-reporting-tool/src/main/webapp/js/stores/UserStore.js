/**
 * Created by ledvima1 on 27.5.15.
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');

var currentUser = null;
var loaded = false;

function loadCurrentUser() {
    Ajax.get('rest/persons/current').end(function (err, resp) {
        UserStore.userLoaded(resp.body);
    });
}

var UserStore = Reflux.createStore({
    listenables: [Actions],
    onLoadUser: function () {
        if (currentUser === null) {
            loadCurrentUser();
        }
    },
    userLoaded: function (user) {
        currentUser = user;
        loaded = true;
        this.trigger(this.getCurrentUser());
    },
    getCurrentUser: function () {
        return currentUser;
    },
    isLoaded: function() {
        return loaded;
    }
});

module.exports = UserStore;
