'use strict';

var createBrowserHistory = require('history/lib/createBrowserHistory');
var createHashHistory = require('history/lib/createHashHistory');
var useBasename = require('history').useBasename;

var Constants = require('../constants/Constants');
var RouterStore = require('../stores/RouterStore');

var Routing = {
    //history: useBasename(createBrowserHistory)({
    //    basename: window.location.pathname
    //}),
    history: createHashHistory(),

    originalTarget: null,

    transitionTo: function (to, query, payload, handlers) {
        RouterStore.setTransitionPayload(to, payload);
        RouterStore.setViewHandlers(to, handlers);
        this.history.pushState(null, to, query);
    },

    transitionToHome: function () {
        this.transitionTo(Constants.HOME_ROUTE);
    },

    saveOriginalTarget: function (url) {
        this.originalTarget = url;
    },

    transitionToOriginalTarget: function () {
        if (this.originalTarget) {
            this.transitionTo(this.originalTarget);
        } else {
            this.transitionTo(Constants.HOME_ROUTE);
        }
    }
};

module.exports = Routing;
