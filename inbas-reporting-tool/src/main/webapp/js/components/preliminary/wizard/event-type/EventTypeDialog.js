/**
 * @jsx
 */

'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Panel = require('react-bootstrap').Panel;
var injectIntl = require('../../../../utils/injectIntl');

var EventTypeTypeahead = require('../../../typeahead/EventTypeTypeahead');
var I18nMixin = require('../../../../i18n/I18nMixin');

var EventTypeDialog = React.createClass({
    mixins: [I18nMixin],

    render: function () {
        return (
            <Modal show={this.props.show} {...this.props} bsSize='small' title='Event Type' animation={false}
                   onHide={this.props.onHide}>
                <Modal.Header closeButton>
                    <Modal.Title>{this.i18n('eventtype.title')}</Modal.Title>
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

module.exports = injectIntl(EventTypeDialog);
