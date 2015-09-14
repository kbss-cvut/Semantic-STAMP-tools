'use strict';

var React = require('react');

var DashboardTile = React.createClass({
    propTypes: {
        onClick: React.PropTypes.func
    },

    onClick: function(e) {
        e.preventDefault();
        this.props.onClick(e);
    },

    render: function() {
        return(
            <button className='dashboard-tile btn-primary btn' onClick={this.props.onClick}>{this.props.children}</button>
        )
    }
});

module.exports = DashboardTile;
