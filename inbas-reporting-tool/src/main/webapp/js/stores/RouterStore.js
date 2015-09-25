'use strict';

var Reflux = require('reflux');

/**
 * Manages passing payloads on routing transition.
 *
 * For example, when one wants to pass and object when transitioning to another route, this store will be used to store
 * the payload object and the target route handler can ask for it.
 */
var RouterStore = Reflux.createStore({

    transitionPayload: {},

    setTransitionPayload: function (route, payload) {
        if (!payload) {
            delete this.transitionPayload[route];
        } else {
            this.transitionPayload[route] = payload;
        }
    },

    /**
     * Gets the specified route's payload, if there is any.
     * @param route Route name
     * @return {*} Route transition payload or null if there is none for the specified route
     */
    getTransitionPayload: function (route) {
        return this.transitionPayload[route];
    }
});

module.exports = RouterStore;
