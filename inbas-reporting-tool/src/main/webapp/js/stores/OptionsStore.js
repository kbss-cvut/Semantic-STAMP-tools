'use strict';

var Reflux = require('reflux');
var jsonld = require('jsonld');

var Actions = require('../actions/Actions');
var Constants = require('../constants/Constants');
var Ajax = require('../utils/Ajax');
var Logger = require('../utils/Logger');
var Utils = require('../utils/Utils');

const URL = 'rest/options';
var options = {};

var OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
    },

    onLoadOptions: function (type, params) {
        if (type) {
            this._loadOptions(type, params);
        } else {
            this._loadOptions(Constants.OPTIONS.OCCURRENCE_CLASS);
        }
    },

    _loadOptions: function (type, params) {
        if (!params) {
            params = {};
        }
        // If there are params, do not reuse the loaded options, they could have been loaded with different params
        if (options[type] && options[type].length !== 0 && !params) {
            this.trigger(type, options[type]);
            return;
        }
        var url = URL + '?type=' + type;
        url = Utils.addParametersToUrl(url, params);
        Ajax.get(url).end(function (data) {
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
