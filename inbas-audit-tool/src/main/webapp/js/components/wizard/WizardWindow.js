/**
 * Created by ledvima1 on 11.6.15.
 */

'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var assign = require('object-assign');

var Wizard = require('./Wizard');

var WizardWindow = React.createClass({
    render: function () {
        var properties = assign({}, this.props, {onClose: this.props.onRequestHide});
        return (
            <Modal {...this.props} bsSize="large" title={this.props.title} animation={false} dialogClassName="large-modal">
                <div className="modal-body" style={{overflow: 'hidden'}}>
                    <Wizard {...properties}/>
                </div>
            </Modal>
        );
    }
});

module.exports = WizardWindow;
