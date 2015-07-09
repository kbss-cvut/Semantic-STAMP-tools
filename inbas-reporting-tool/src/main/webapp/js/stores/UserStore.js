/**
 * Created by ledvima1 on 27.5.15.
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');

var currentUser = null;
var loaded = false;

function loadCurrentUser() {
    request.get('rest/persons/current').accept('json').end(function (err, resp) {
        if (err) {
            console.log(err.status, err.response);
            return;
        }
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
