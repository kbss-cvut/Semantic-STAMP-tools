/**
 * Created by ledvima1 on 26.5.15.
 */

var Reflux = require('reflux');
var Actions = require('../actions/Actions');
var request = require('superagent');


var reports = [];

function loadReports() {
    request.get('rest/reports').accept('json').end(function (err, resp) {
        if (err) {
            if (err.status !== 404) {
                console.log(err.status, err.response);
            }
            return;
        }
        ReportsStore.onReportsLoaded(resp.body);
    });
}

var ReportsStore = Reflux.createStore({
    init: function () {
        loadReports();
    },
    getCurrentState: function () {
        return {
            reports: reports
        };
    },
    getReports: function () {
        return reports;
    },
    onReportsLoaded: function (data) {
        reports = data;
        this.trigger(this.getCurrentState());
    }
});

module.exports = ReportsStore;
