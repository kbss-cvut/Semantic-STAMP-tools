'use strict';

var Logger = require('../../utils/Logger');

/**
 * Mixin for error handling in stores.
 *
 * It just logs the error into the console.
 */
var ErrorHandlingMixin = {
    handleError: function (err) {
        try {
            var error = JSON.parse(err.response.text),
                method = err.response.req.method,
                msg = method + ' ' + error.requestUri + ' - Status ' + err.status + ': ' + error.message;
            if (err.status === 404) {
                Logger.warn(msg);
            } else {
                Logger.error(msg);
            }
        } catch (err) {
            Logger.error('AJAX error: ' + err.response.text);
        }
    }
};

module.exports = ErrorHandlingMixin;
