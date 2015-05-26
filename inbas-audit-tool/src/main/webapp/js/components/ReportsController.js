/**
 * Created by ledvima1 on 26.5.15.
 */

var React = require('react');
var Reflux = require('reflux');
var ReportsStore = require('../stores/ReportsStore');
var Reports = require('./Reports');

var ReportsController = React.createClass({
    mixins: [Reflux.listenTo(ReportsStore, 'onChange')],
    getInitialState: function() {
        return {
            reports: ReportsStore.getReports()
        };
    },
    onChange: function(newState) {
        this.setState(newState);
    },
    render: function() {
        return (
            <Reports reports={this.state.reports} />
        );
    }
});

module.exports = ReportsController;
