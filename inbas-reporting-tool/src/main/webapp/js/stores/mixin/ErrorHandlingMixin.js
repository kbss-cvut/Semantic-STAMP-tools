'use strict';

/**
 * Mixin for error handling in stores.
 *
 * It just logs the error into the console.
 */
var ErrorHandlingMixin = {
    handleError: function (err) {
        try {
            var error = JSON.parse(err.response.text);
            console.log(error.requestUri + ' - Status ' + err.status + ': ' + error.message);
        } catch (err) {
            console.error('AJAX error: ' + err.response.text);
        }
    }
};

module.exports = ErrorHandlingMixin;
