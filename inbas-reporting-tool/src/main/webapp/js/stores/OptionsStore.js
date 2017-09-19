'use strict';

const Reflux = require('reflux');
const jsonld = require('jsonld');

const Actions = require('../actions/Actions');
const Constants = require('../constants/Constants');
const Ajax = require('../utils/Ajax');
const Logger = require('../utils/Logger');

const options = {};

const OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
    },

    onLoadOptions: function (type) {
        if (type) {
            this._loadOptions(type);
        } else {
            this._loadOptions(Constants.OPTIONS.OCCURRENCE_CLASS);
        }
    },

    _loadOptions: function (type) {
        if (options[type] && options[type].length !== 0) {
            this.trigger(type, options[type]);
            return;
        }
        Ajax.get(Constants.REST_PREFIX + 'options?type=' + type).end(function (data) {
            if (data.length > 0) {
                jsonld.frame(data, {}, null, function (err, framed) {
                    options[type] = framed['@graph'];
                    this.trigger(type, options[type]);
                }.bind(this));
            } else {
                Logger.warn('No data received when loading options of type ' + type + '.');
                this.trigger(type, this.getOptions(type));
            }

        }.bind(this), function () {
            this.trigger(type, this.getOptions(type));
        }.bind(this));
    },

    getOptions: function (type) {
        return options[type] ? options[type] : [];
    }
});

module.exports = OptionsStore;
