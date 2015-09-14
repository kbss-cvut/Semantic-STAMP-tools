'use strict';

var React = require('react');
var Reflux = require('reflux');

var router = require('../../utils/router');
var UserStore = require('../../stores/UserStore');
var Dashboard = require('./Dashboard');

var DashboardController = React.createClass({
    mixins: [Reflux.listenTo(UserStore, 'onUserLoaded')],
    getInitialState: function () {
        return {
            firstName: UserStore.getCurrentUser() ? UserStore.getCurrentUser().firstName : ''
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    createEmptyReport: function () {
        router.transitionTo('report_new', null, {onSuccess: 'reports', onCancel: 'dashboard'});
    },

    importInitialReport: function () {

    },

    openReport: function (report) {
        router.transitionTo('report', {reportKey: report.key}, {onSuccess: 'reports', onCancel: 'dashboard'});
    },

    showReports: function () {
        router.transitionTo('reports');
    },


    render: function () {
        return (<Dashboard userFirstName={this.state.firstName} showAllReports={this.showReports}
                           createEmptyReport={this.createEmptyReport} importInitialReport={this.importInitialReport}
                           openReport={this.openReport}/>);
    }
});

module.exports = DashboardController;
