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
    listenables: [Actions],
    getCurrentState: function () {
        return {
            reports: reports
        };
    },
    getReports: function () {
        return reports;
    },
    onLoadReports: function() {
        loadReports();
    },
    onReportsLoaded: function (data) {
        reports = data;
        this.trigger(this.getCurrentState());
    },
    onCreateReport: function(report) {
        request.post('rest/reports').send(report).type('json').end(function(err, res) {
            if (err) {
                console.log(err.status, err.response);
            } else {
                loadReports();
            }
        })
    }
});

module.exports = ReportsStore;
