/**
 * Created by ledvima1 on 21.5.15.
 */

var React = require('react');
var request = require('superagent');

var HelloWorld = React.createClass({
    getInitialState: function () {
        return {
            hello: ''
        };
    },
    componentDidMount: function () {
        request('rest/helloworld').set('Accept', 'text/plain').end(function (err, resp) {
            if (err) {
                console.log(err, resp.status, resp.text);
            }
            this.setState({hello: resp.text});
        }.bind(this));
    },
    render: function () {
        return (
            <h1>The server says: {this.state.hello}</h1>
        );
    }
});

module.exports = HelloWorld;
