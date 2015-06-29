/**
 * Created by ledvima1 on 29.6.15.
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');

var eventTypes = [];

var EventTypeStore = Reflux.createStore({
    init: function() {
        this.listenTo(Actions.loadEventTypes, this.onLoadEventTypes);
    },
    onLoadEventTypes: function() {
        if (eventTypes.length !== 0) {
            this.trigger(eventTypes);
            return;
        }
        request.get('rest/eventTypes').accept('json').end(function(err, resp) {
            if (err) {
                if (err.status !== 404) {
                    console.log('Unable to load event types. Got status ' + err.status);
                }
            } else {
                eventTypes = resp.body[0].nodes;
            }
            this.trigger(eventTypes);
        }.bind(this));
    }
});

module.exports = EventTypeStore;
