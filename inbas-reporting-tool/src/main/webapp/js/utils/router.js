/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var router;

module.exports = {

    originalTarget: null,

    saveOriginalTarget: function(url) {
        this.originalTarget = url;
    },

    transitionToOriginalTarget: function() {
        if (this.originalTarget) {
            this.transitionTo(this.originalTarget);
        } else {
            this.transitionTo('home');
        }
    },

    makePath: function (to, params, query) {
        return router.makePath(to, params, query);
    },

    makeHref: function (to, params, query) {
        return router.makeHref(to, params, query);
    },

    transitionTo: function (to, params, query) {
        if (UserStore.isLoaded()) {
            this.saveOriginalTarget(to);
            this.transitionTo('login');
            return;
        }
        router.transitionTo(to, params, query);
    },

    replaceWith: function (to, params, query) {
        router.replaceWith(to, params, query);
    },

    goBack: function () {
        router.goBack();
    },

    run: function (render) {
        router.run(render);
    }
};


var routes = require('./Routes'),
    Router = require('react-router');
var UserStore = require('../stores/UserStore');

router = Router.create({
    routes: routes,
    location: null
});
