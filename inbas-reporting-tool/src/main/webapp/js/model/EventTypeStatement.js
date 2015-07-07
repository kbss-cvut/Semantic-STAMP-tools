/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var assign = require('object-assign');

/**
 * Base event type assessment class.
 * @param data
 * @constructor
 */
var EventTypeAssessment = function (data) {
    assign(this, data);
};

EventTypeAssessment.prototype.toString = function () {
    return this.description ? this.description : '';
};

module.exports = EventTypeAssessment;
