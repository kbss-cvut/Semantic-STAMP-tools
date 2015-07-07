/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var router;

module.exports = {

    makePath(to, params, query) {
        return router.makePath(to, params, query);
    },

    makeHref(to, params, query) {
        return router.makeHref(to, params, query);
    },

    transitionTo(to, params, query) {
        router.transitionTo(to, params, query);
    },

    replaceWith(to, params, query) {
        router.replaceWith(to, params, query);
    },

    goBack() {
        router.goBack();
    },

    run(render) {
        router.run(render);
    }
};


var routes = require('./Routes'),
    Router = require('react-router');

router = Router.create({
    routes: routes,
    location: null
});
