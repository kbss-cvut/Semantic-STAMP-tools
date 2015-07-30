/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;

var LocationTypeahead = require('../../../../LocationTypeahead');

var IncursionLocation = React.createClass({
    getInitialState: function () {
        var incursionLocation = this.props.data.statement.location;
        return {
            location: incursionLocation ? incursionLocation.name : ''
        };
    },

    onChange: function (option) {
        var statement = this.props.data.statement;
        if (option.label === '' && statement.location) {
            delete statement.location;
        } else {
            statement.location = {
                uri: option.id,
                name: option.name
            }
        }
        this.setState({location: option.name});
    },

    // Rendering

    render: function () {
        var title = (<h3>Incursion location</h3>);
        var location = this.props.data.statement.location;
        return (
            <Panel header={title}>
                <div>
                    <LocationTypeahead name='incursionLocation' onChange={this.onChange}
                                       value={location ? location.name : null}/>
                </div>
            </Panel>
        );
    }
});

module.exports = IncursionLocation;
