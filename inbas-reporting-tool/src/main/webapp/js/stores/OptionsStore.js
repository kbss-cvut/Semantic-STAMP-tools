/**
 * @author ledvima1
 */

'use strict';

var Reflux = require('reflux');
var request = require('superagent');

var Actions = require('../actions/Actions');

var lvpOptions = [];

var OptionsStore = Reflux.createStore({
    init: function() {
        this.listenTo(Actions.loadLvpOptions, this.onLoadLvpOptions);
    },

    onLoadLvpOptions: function() {
        if (lvpOptions.length !== 0) {
            this.trigger(lvpOptions);
            return;
        }
        request.get('rest/options/lvp').accept('json').end(function(err, resp) {
            if (err) {
                if (err.status !== 404) {
                    console.log('Unable to load Low visibility procedure options. Got status ' + err.status);
                }
            } else {
                lvpOptions = resp.body;
            }
            this.trigger(lvpOptions);
        }.bind(this));
    }
});

module.exports = OptionsStore;
