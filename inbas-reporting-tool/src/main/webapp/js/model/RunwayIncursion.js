'use strict';

var EventTypeStatement = require('./EventTypeStatement');


/**
 * Runway incursion.
 *
 * Extends the EventTypeStatement class.
 * @param data
 * @constructor
 */
function RunwayIncursion(data) {
    EventTypeStatement.call(this, data);
}

RunwayIncursion.prototype = Object.create(EventTypeStatement.prototype);

RunwayIncursion.prototype.constructor = RunwayIncursion;

RunwayIncursion.prototype.toString = function () {
    var intruder = '';
    switch (this.intruder.intruderType) {
        case 'aircraft':
            intruder = 'An aircraft (call sign ' + this.intruder.callSign + ')';
            break;
        case 'vehicle':
            intruder = 'A vehicle (call sign ' + this.intruder.callSign + ')';
            break;
        case 'person':
            intruder = 'A person (organization ' + this.intruder.organization + ')';
            break;
        default:
            break;
    }
    return intruder + ' intruded on the runway.'
};

module.exports = RunwayIncursion;
