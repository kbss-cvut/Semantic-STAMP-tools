/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-typeahead').Typeahead;

var Actions = require('../actions/Actions');
var TypeaheadStore = require('../stores/TypeaheadStore');

var LocationTypeahead = React.createClass({
    propTypes: {
        name: React.PropTypes.string,
        value: React.PropTypes.string,
        onChange: React.PropTypes.func
    },

    mixins: [Reflux.ListenerMixin],

    getInitialState: function () {
        return {
            options: []
        };
    },

    componentWillMount: function () {
        this.listenTo(TypeaheadStore, this.onLocationsLoaded);
        Actions.loadLocations();
    },

    onLocationsLoaded: function () {
        var locations = TypeaheadStore.getLocations();
        var options = [];
        for (var i = 0, len = locations.length; i < len; i++) {
            var loc = locations[i].id;
            var name = loc.substring(loc.lastIndexOf('/') + 1);
            options.push({id: locations[i].id, name: name});
        }
        this.setState({options: options});
    },

    onOptionSelected: function (option) {
        this.props.onChange(option);
    },

    render: function () {
        var classes = {
            input: 'form-control',
            listItem: 'btn-link item',
            results: 'autocomplete-results'
        };
        return (<Typeahead ref='locationSelect' name={this.props.name} formInputOption='id' placeholder='Location'
                           onOptionSelected={this.onOptionSelected} filterOption='name' displayOption='name'
                           value={this.props.value ? this.props.value : null}
                           options={this.state.options} customClasses={classes}/>);
    }
});

module.exports = LocationTypeahead;
