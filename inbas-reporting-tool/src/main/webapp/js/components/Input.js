/**
 * @author kidney
 */

'use strict';

var React = require('react');
var BootstrapInput = require('react-bootstrap').Input;

var Input = React.createClass({
    render: function () {
        if (this.props.type === 'textarea') {
            return <BootstrapInput bsSize='small' style={{height: 'auto'}} {...this.props}/>;
        } else {
            return <BootstrapInput bsSize='small' {...this.props}/>;
        }
    }
});

module.exports = Input;
