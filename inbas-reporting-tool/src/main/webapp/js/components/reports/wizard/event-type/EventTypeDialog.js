/**
 * Created by ledvima1 on 24.6.15.
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var Modal = require('react-bootstrap').Modal;
var Panel = require('react-bootstrap').Panel;
var TypeAhead = require('react-typeahead').Typeahead;


var Actions = require('../../../../actions/Actions');
var TypeaheadStore = require('../../../../stores/TypeaheadStore');
var TypeaheadResultList = require('../../../typeahead/TypeaheadResultList');

var EventTypeDialog = React.createClass({
    mixins: [Reflux.ListenerMixin],
    getInitialState: function () {
        return {
            options: []
        };
    },
    componentDidMount: function () {
        this.listenTo(TypeaheadStore, this.onEventsLoaded);
        Actions.loadEventTypes();
    },
    onEventsLoaded: function () {
        this.setState({options: TypeaheadStore.getEventTypes()});
    },
    onSelect: function (option) {
        this.props.onTypeSelect(option);
    },
    render: function () {
        return (
            <Modal show={this.props.show} {...this.props} bsSize='small' title='Event Type' animation={false}
                   onHide={this.props.onHide}>
                <Modal.Header closeButton>
                    <Modal.Title>Event Type</Modal.Title>
                </Modal.Header>
                <Panel>
                    <div className='centered'>
                        <TypeAheadWrapper onSelect={this.onSelect} options={this.state.options}/>
                    </div>
                </Panel>
            </Modal>
        );
    }
});

/**
 * This is just a workaround to be able to focus the TypeAhead once the modal window is open.
 */
var TypeAheadWrapper = React.createClass({
    componentDidMount: function () {
        this.refs.eventTypeSelect.focus();
    },
    render: function () {
        var classes = {
            input: 'form-control',
            listItem: 'btn-link item',
            results: 'autocomplete-results'
        };
        return (
            <TypeAhead ref='eventTypeSelect' className='form-group form-group-sm' name='eventType' formInputOption='id'
                       placeholder='Event Type' onOptionSelected={this.props.onSelect} filterOption='name'
                       displayOption='name' options={this.props.options} customClasses={classes}
                       customListComponent={TypeaheadResultList}/>);
    }
});

module.exports = EventTypeDialog;
