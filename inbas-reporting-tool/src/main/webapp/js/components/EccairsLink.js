/**
 * @jsx
 */
'use strict';

var React = require('react');

var ECCAIRS_URL = require('../constants/Constants').ECCAIRS_URL;

var EccairsLink = React.createClass({

    propTypes: {
        text: React.PropTypes.string.isRequired,
        type: React.PropTypes.string
    },

    render: function () {
        var title = 'ECCAIRS ' + (this.props.type ? this.props.type : 'attribute') + ' ' + this.props.text;
        return (
            <a href={ECCAIRS_URL} title={title} target='_blank'>#{this.props.text}</a>);
    }
});

module.exports = EccairsLink;
