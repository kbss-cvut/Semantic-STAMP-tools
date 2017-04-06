'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');
const Logger = require('../utils/Logger');

const BASE_URL = 'rest/statistics';

const StatisticsStore = Reflux.createStore({
    listenables: [Actions],

    onLoadStatistics: function (queryName) {
        Ajax.get(BASE_URL+"/"+queryName).end(function (data) {
            this.trigger({
                action: Actions.loadStatistics,
                queryName: queryName,
                queryResults: data
            });
        }.bind(this), function () {
            Logger.error('Unable to fetch statistics.');
            this.trigger({
                action: Actions.loadStatistics,
                queryName: queryName,
                queryResults: []
            });
        }.bind(this));
    }
});

module.exports = StatisticsStore;