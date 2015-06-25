/**
 * Created by ledvima1 on 24.6.15.
 */

'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Panel = require('react-bootstrap').Panel;

var Select = require('../../../Select');

var EventTypeDialog = React.createClass({
    onSelect: function(e) {
        e.stopPropagation();
        this.props.onChange(e);
        this.props.onRequestHide();
    },
    render: function () {
        var options = [
            {value: 'runway_incursion', label: 'Runway Incursion'}
        ];
        return (
            <Modal {...this.props} bsSize='small' title='Event Type' animation={false}>
                <Panel>
                    <div className='centered'>
                        <Select label='Event Type' onChange={this.onSelect} options={options}/>
                    </div>
                </Panel>
            </Modal>
        );
    }
});

module.exports = EventTypeDialog;
