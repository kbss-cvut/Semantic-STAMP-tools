/**
 * @jsx
 */
'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-bootstrap-typeahead');
var injectIntl = require('../../utils/injectIntl');

var Actions = require('../../actions/Actions');
var TypeaheadResultList = require('./EventTypeTypeaheadResultList');
var TypeaheadStore = require('../../stores/TypeaheadStore');
var I18nMixin = require('../../i18n/I18nMixin');
var Vocabulary = require('../../constants/Vocabulary');

var EventTypeTypeahead = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],

    propTypes: {
        onSelect: React.PropTypes.func.isRequired
    },

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
        var options = TypeaheadStore.getEventTypes();
        options = options.map(function (item) {
            return {
                id: item['@id'],
                type: item['@type'],
                name: item[Vocabulary.RDFS_LABEL],
                description: item[Vocabulary.RDFS_COMMENT]
            }
        });
        this.setState({options: options});
    },

    focus: function () {
        this.refs.eventTypeSelect.focus();
    },

    render: function () {
        var classes = {
            input: 'form-control'
        };
        var label = this.props.label ? (<label className='control-label'>{this.props.label}</label>) : null,
            value = this.props.value ? this.props.value : null,
            placeholder = this.props.placeholder ? this.props.placeholder : this.i18n('eventtype.title');
        return (
            <div>
                {label}
                <Typeahead ref='eventTypeSelect' className='form-group form-group-sm' name='eventType'
                           formInputOption='id' placeholder={placeholder} onOptionSelected={this.props.onSelect}
                           filterOption='name' value={value} displayOption='name' options={this.state.options}
                           customClasses={classes} customListComponent={TypeaheadResultList}/>
            </div>);
    }
});

module.exports = injectIntl(EventTypeTypeahead);
