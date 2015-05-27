/**
 * Created by ledvima1 on 26.5.15.
 */

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');

var ReportsStore = require('../stores/ReportsStore');
var UserStore = require('../stores/UserStore');
var Reports = require('./Reports');

var ReportsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onChange'),
        Reflux.listenTo(UserStore, 'onChange')],
    getInitialState: function () {
        return {
            reports: ReportsStore.getReports()
        };
    },
    onChange: function (newState) {
        this.setState(assign({}, this.state, newState));
    },
    render: function () {
        return (
            <Reports reports={this.state.reports} user={this.state.user} />
        );
    }
});

module.exports = ReportsController;
