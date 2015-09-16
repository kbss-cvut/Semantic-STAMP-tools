'use strict';

var Constants = require('../constants/Constants');

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
            this.transitionTo(Constants.HOME_ROUTE);
        }
    },

    makePath: function (to, params, query) {
        return router.makePath(to, params, query);
    },

    makeHref: function (to, params, query) {
        return router.makeHref(to, params, query);
    },

    transitionTo: function (to, params, query) {
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

router = Router.create({
    routes: routes,
    location: null
});
