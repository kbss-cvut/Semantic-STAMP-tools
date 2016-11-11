'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Constants = require('../constants/Constants');

const BASE_URL = 'rest/arms';

var ArmsStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.calculateArmsIndex, this.onCalculateArmsIndex);
        this.listenTo(Actions.calculateSira, this.onCalculateSira);
    },

    onCalculateArmsIndex: function (arms) {
        Ajax.get(BASE_URL + '?' + Constants.ARMS.ACCIDENT_OUTCOME + '=' + arms.accidentOutcome +
            '&' + Constants.ARMS.BARRIER_EFFECTIVENESS + '=' + arms.barrierEffectiveness).end((data) => {
            this.trigger({
                action: Actions.calculateArmsIndex,
                armsIndex: Number(data)
            });
        });
    },

    onCalculateSira: function (sira) {
        Ajax.post(BASE_URL + '/sira').send(sira).end((data) => {
            this.trigger({
                action: Actions.calculateSira,
                sira: data
            });
        });
    }
});

module.exports = ArmsStore;
