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
        var properties = assign({}, this.props, {onCancel: this.props.onRequestHide});
        return (
            <Modal {...this.props} bsSize="large" title={this.props.title} animation={false}>
                <div className="modal-body">
                    <Wizard {...this.props}/>
                </div>
            </Modal>
        );
    }
});

module.exports = WizardWindow;
