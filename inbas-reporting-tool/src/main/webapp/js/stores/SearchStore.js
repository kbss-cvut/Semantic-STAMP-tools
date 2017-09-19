'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
const Ajax = require('../utils/Ajax');
const Constants = require('../constants/Constants');

const SearchStore = Reflux.createStore({
    init: function () {
        this.listenTo(Actions.fullTextSearch, this.onFullTextSearch);
    },

    onFullTextSearch: function (expr) {
        Ajax.get(Constants.REST_PREFIX + 'search?expression=' + encodeURIComponent(expr)).end((data) => {
            this.trigger({
                action: Actions.fullTextSearch,
                data: data
            });
        });
    }
});

module.exports = SearchStore;
