/**
 * @author ledvima1
 */

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;

var Home = React.createClass({
    render: function () {
        return (
            <Jumbotron>
                <h2>Welcome</h2>

                <p>
                    Welcome to the INBAS Audit Tool. You can view a list of currently existing event reports and create
                    new reports in the Reports tab.
                </p>
            </Jumbotron>
        );
    }
});

module.exports = Home;
