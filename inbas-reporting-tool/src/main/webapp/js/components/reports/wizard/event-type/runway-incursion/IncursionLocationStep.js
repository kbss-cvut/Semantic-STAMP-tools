/**
 * @author ledvima1
 */

'use strict';

var React = require('react');

var LocationTypeahead = require('../../../../typeahead/LocationTypeahead');

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
        var location = this.props.data.statement.location;
        return (
            <div className='form-group'>
                <LocationTypeahead name='incursionLocation' onChange={this.onChange}
                                   value={location ? location.name : null}/>
            </div>
        );
    }
});

module.exports = IncursionLocation;
