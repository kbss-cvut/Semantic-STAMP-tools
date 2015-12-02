/**
 * @jsx
 */

'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Panel = require('react-bootstrap').Panel;

var EventTypeTypeahead = require('../../../typeahead/EventTypeTypeahead');

var EventTypeDialog = React.createClass({

    render: function () {
        return (
            <Modal show={this.props.show} {...this.props} bsSize='small' title='Event Type' animation={false}
                   onHide={this.props.onHide}>
                <Modal.Header closeButton>
                    <Modal.Title>Event Type</Modal.Title>
                </Modal.Header>
                <Panel>
                    <div className='centered'>
                        <EventTypeTypeahead ref='eventType' onSelect={this.props.onTypeSelect} focus={true}/>
                    </div>
                </Panel>
            </Modal>
        );
    }
});

module.exports = EventTypeDialog;
