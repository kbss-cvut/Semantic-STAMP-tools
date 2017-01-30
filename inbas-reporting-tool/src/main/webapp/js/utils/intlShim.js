'use strict';

module.exports = function(callback) {
    // shim for Intl needs to be loaded dynamically
    // so we callback when we're done to represent
    // some kind of "intlReady" event
    if (!window.Intl) {
        require(['intl/Intl'], function(Intl) {
            window.Intl = Intl;
            callback();
        });
    } else {
        setTimeout(callback, 0); // force async
    }
};
