'use strict';

//var createBrowserHistory = require('history/lib/createBrowserHistory');
//var useBasename = require('history').useBasename;
var createHashHistory = require('history/lib/createHashHistory');

var Constants = require('../constants/Constants');
var RouterStore = require('../stores/RouterStore');

var Routing = {
    //history: useBasename(createBrowserHistory)({
    //    basename: window.location.pathname
    //}),
    history: createHashHistory(),

    originalTarget: null,

    /**
     * Transitions to the specified route
     * @param route Route object
     * @param options Transition options, can specify path parameters, query parameters, payload and view handlers.
     */
    transitionTo: function (route, options) {
        var path = route.path;
        if (!options) {
            options = {};
        }
        if (options.params) {
            path = this.setPathParams(path, options.params);
        }
        RouterStore.setTransitionPayload(route.name, options.payload);
        RouterStore.setViewHandlers(route.name, options.handlers);
        this.history.pushState(null, path, options.query);
    },

    setPathParams: function (path, params) {
        for (var paramName in params) {
            if (params.hasOwnProperty(paramName)) {
                path = path.replace(':' + paramName, params[paramName]);
            }
        }
        return path;
    },

    transitionToHome: function () {
        this.transitionTo(Constants.HOME_ROUTE);
    },

    saveOriginalTarget: function (route) {
        this.originalTarget = route;
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
