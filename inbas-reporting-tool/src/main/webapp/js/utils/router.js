'use strict';

var Constants = require('../constants/Constants');
var RouterStore = require('../stores/RouterStore');

var router;

module.exports = {

    originalTarget: null,

    saveOriginalTarget: function (url) {
        this.originalTarget = url;
    },

    transitionToOriginalTarget: function () {
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

    /**
     * Transitions to the specified route.
     * @param to Target route name
     * @param params (Optional) Path parameters
     * @param query (Optional) Query parameters
     * @param payload (Optional) Payload to pass to router store to make it accessible from the target route handler
     */
    transitionTo: function (to, params, query, payload) {
        RouterStore.setTransitionPayload(to, payload);
        router.transitionTo(to, params, query);
    },

    transitionToHome: function () {
        this.transitionTo(Constants.HOME_ROUTE);
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
