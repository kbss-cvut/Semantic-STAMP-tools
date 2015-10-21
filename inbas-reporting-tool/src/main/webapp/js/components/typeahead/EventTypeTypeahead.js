/**
 * @jsx
 */
'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-typeahead').Typeahead;

var Actions = require('../../actions/Actions');
var TypeaheadResultList = require('./EventTypeTypeaheadResultList');
var TypeaheadStore = require('../../stores/TypeaheadStore');

var EventTypeTypeahead = React.createClass({

    propTypes: {
        onSelect: React.PropTypes.func.isRequired
    },

    mixins: [Reflux.ListenerMixin],
    getInitialState: function () {
        return {
            options: []
        };
    },

    componentDidMount: function () {
        this.listenTo(TypeaheadStore, this.onEventsLoaded);
        Actions.loadEventTypes();
        if (this.props.focus) {
            this.focus();
        }
    },
    onEventsLoaded: function () {
        this.setState({options: TypeaheadStore.getEventTypes()});
    },

    focus: function () {
        this.refs.eventTypeSelect.focus();
    },

    render: function () {
        var classes = {
            input: 'form-control'
        };
        var label = this.props.label ? (<label className='control-label'>{this.props.label}</label>) : null;
        return (
            <div>
                {label}
                <Typeahead ref='eventTypeSelect' className='form-group form-group-sm' name='eventType'
                           formInputOption='id' placeholder='Event Type' onOptionSelected={this.props.onSelect}
                           filterOption='name' value={this.props.value ? this.props.value : null}
                           displayOption='name' options={this.state.options} customClasses={classes}
                           customListComponent={TypeaheadResultList}/>
            </div>);
    }
});

module.exports = EventTypeTypeahead;
