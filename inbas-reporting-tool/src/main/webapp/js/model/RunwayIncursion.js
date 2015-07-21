/**
 * Created by kidney on 7/7/15.
 */

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
            intruder = 'an aircraft (call sign ' + this.intruder.callSign + ')';
            break;
        case 'vehicle':
            intruder = 'a vehicle (call sign ' + this.intruder.callSign + ')';
            break;
        case 'person':
            intruder = 'a person (organization ' + this.intruder.organization + ')';
            break;
        default:
            break;
    }
    return 'Flight ' + this.clearedAircraft.flightNumber + ' was cleared to use runway, but ' + intruder +
        ' intruded on the runway.'
};

module.exports = RunwayIncursion;
