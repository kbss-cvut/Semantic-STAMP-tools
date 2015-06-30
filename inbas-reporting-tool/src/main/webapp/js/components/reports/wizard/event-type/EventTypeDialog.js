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
var EventTypeStore = require('../../../../stores/EventTypeStore');
var Select = require('../../../Select');

var EventTypeDialog = React.createClass({
    mixins: [Reflux.ListenerMixin],
    getInitialState: function () {
        return {
            options: []
        };
    },
    componentDidMount: function () {
        this.listenTo(EventTypeStore, this.onEventsLoaded);
        Actions.loadEventTypes();
    },
    onEventsLoaded: function (eventTypes) {
        this.setState({options: eventTypes});
    },
    onSelect: function (option) {
        this.props.onTypeSelect(option);
        // We're getting errors when we call the onRequestHide immediately on select
        setTimeout(this.props.onRequestHide, 100);

    },
    render: function () {
        var classes = {
            input: 'form-control',
            listItem: 'btn-link',
            results: 'autocomplete-results'
        };
        return (
            <Modal {...this.props} bsSize='small' title='Event Type' animation={false}>
                <Panel>
                    <div className='centered'>
                        <TypeAhead name='eventType' formInputOption='id' placeholder='Event Type'
                                   onOptionSelected={this.onSelect} filterOption='name' displayOption='name'
                                   options={this.state.options} customClasses={classes}/>
                    </div>
                </Panel>
            </Modal>
        );
    }
});

module.exports = EventTypeDialog;
