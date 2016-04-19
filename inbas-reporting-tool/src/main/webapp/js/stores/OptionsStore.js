'use strict';

var Reflux = require('reflux');
var jsonld = require('jsonld');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Logger = require('../utils/Logger');

var occurrenceClasses = [];

var OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
    },

    onLoadOptions: function () {
        this._loadOccurrenceClassOptions();
    },

    _loadOccurrenceClassOptions: function () {
        if (occurrenceClasses.length !== 0) {
            this.trigger('occurrenceClass', occurrenceClasses);
            return;
        }
        Ajax.get('rest/options?type=occurrenceClass').end(function (data) {
            if (data.length > 0) {
                jsonld.frame(data, {}, null, function (err, framed) {
                    occurrenceClasses = framed['@graph'];
                    this.trigger('occurrenceClass', occurrenceClasses);
                }.bind(this));
            } else {
                Logger.warn('No data received when loading occurrence classes.');
                this.trigger('occurrenceClass', occurrenceClasses);
            }

        }.bind(this), function () {
            this.trigger('occurrenceClass', occurrenceClasses);
        }.bind(this));
    },

    getOccurrenceClasses: function () {
        return occurrenceClasses;
    }
});

module.exports = OptionsStore;
