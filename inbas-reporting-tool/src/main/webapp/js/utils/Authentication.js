/**
 * @author ledvima1
 */

'use strict';

var Actions = require('../actions/Actions');
var Ajax = require('./Ajax');
var router = require('./router');

var Authentication = {

    login: function (username, password, errorCallback) {
        Ajax.post('j_spring_security_check', null, 'form')
            .send('username=' + username).send('password=' + password)
            .end(function (err, resp) {
                if (err) {
                    errorCallback();
                    return;
                }
                var status = JSON.parse(resp.text);
                if (!status.success || !status.loggedIn) {
                    errorCallback();
                    return;
                }
                Actions.loadUser();
                console.log('User successfully authenticated.');
                router.transitionToOriginalTarget();
            }.bind(this));
    },

    logout: function () {
        Ajax.post('j_spring_security_logout').end(function (err, resp) {
            if (err) {
                console.log('Logout failed. Status: ' + err.status);
            }
            window.location.reload();
        });
    }
};

module.exports = Authentication;
