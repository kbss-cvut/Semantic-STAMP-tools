/**
 * @author ledvima1
 */

'use strict';

var request = require('superagent');
var Cookies = require('js-cookie');

var router = require('./router');

var csrfToken = 'X-CSRF-Token';

var Ajax = {
    req: null,

    getCsrfToken: function () {
        var cookie = Cookies.get('CSRF-TOKEN');
        return cookie ? cookie : '';
    },

    get: function (url) {
        this.req = request.get(url, null, null).accept('json');
        return this;
    },

    post: function (url, data, type) {
        this.req = request.post(url).type(type ? type : 'json').accept('json');
        if (data) {
            this.req = this.req.send(data);
        }
        return this;
    },

    put: function (url, data) {
        this.req = request.put(url).type('json');
        if (data) {
            this.req = this.req.send(data);
        }
        return this;
    },

    del: function (url) {
        this.req = request.del(url);
        return this;
    },

    send: function (data) {
        this.req = this.req.send(data);
        return this;
    },

    end: function (fn) {
        this.req.set(csrfToken, this.getCsrfToken()).end(function (err, resp) {
            if (err) {
                if (err.status === 401) {
                    var currentRoute = window.location.hash.substr(1);
                    router.saveOriginalTarget(currentRoute);
                    router.transitionTo('login');
                    return;
                }
            }
            fn(err, resp);
        });
    }
};

module.exports = Ajax;
