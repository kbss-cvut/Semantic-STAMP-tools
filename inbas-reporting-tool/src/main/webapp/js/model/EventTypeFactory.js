/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var EventTypeStatement = require('./EventTypeStatement');
var RunwayIncursion = require('./RunwayIncursion');

module.exports = {
    create: function (data) {
        if (data.intruder) {
            return new RunwayIncursion(data);
        } else {
            return new EventTypeStatement(data);
        }
    }
};
