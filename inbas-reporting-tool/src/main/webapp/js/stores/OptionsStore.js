'use strict';

const Reflux = require('reflux');
const jsonld = require('jsonld');

const Actions = require('../actions/Actions');
const Constants = require('../constants/Constants');
const Ajax = require('../utils/Ajax');
const Logger = require('../utils/Logger');
const Utils = require('../utils/Utils');

const URL = 'rest/options';
const STATISTICS_URL = URL + '/statistics/config';
const options = {};
let statisticsConfig = null;

const OptionsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.loadOptions, this.onLoadOptions);
        this.listenTo(Actions.loadStatisticsConfig, this.onLoadStatisticsConfig);
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
        let url = URL + '?type=' + type;
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

    onLoadStatisticsConfig: function () {
        if (statisticsConfig) {
            this.trigger({
                action: Actions.loadStatisticsConfig,
                config: statisticsConfig
            });
            return;
        }
        Ajax.get(STATISTICS_URL).end(data => {
            if (data.configuration) {
                statisticsConfig = data.configuration;
            }
            this.trigger({
                action: Actions.loadStatisticsConfig,
                config: statisticsConfig
            });
        });
    },

    getOptions: function (type) {
        return options[type] ? options[type] : [];
    },

    getStatisticsConfiguration: function() {
        return statisticsConfig ? statisticsConfig : {};
    }
});

module.exports = OptionsStore;
