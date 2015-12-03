/**
 * @jsx
 */
'use strict';

var React = require('react');
var Alert = require('react-bootstrap').Alert;
var Button = require('react-bootstrap').Button;

var Routing = require('../utils/Routing');

/**
 * Shows alert with message informing that a resource could not be found.
 *
 * Closing the alert transitions the user to the application's home.
 */
var ResourceNotFound = React.createClass({

    propTypes: {
        resource: React.PropTypes.string.isRequired,
        identifier: React.PropTypes.object
    },

    onClose: function () {
        Routing.transitionToHome();
    },

    render: function () {
        var text;
        if (this.props.identifier) {
            text = this.props.resource + ' with id ' + this.props.identifier + ' not found.';
        } else {
            text = this.props.resource + ' not found.';
        }
        return (<Alert bsStyle='danger' onDismiss={this.onClose}>
            <h4>Not Found</h4>

            <p>{text}</p>

            <p>
                <Button onClick={this.onClose}>Close</Button>
            </p>
        </Alert>);
    }
});

module.exports = ResourceNotFound;
