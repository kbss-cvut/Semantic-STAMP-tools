'use strict';

const React = require('react');
const Alert = require('react-bootstrap').Alert;

const Constants = require('../../constants/Constants');

const MessageMixin = {
    getInitialState: function () {
        return {
            message: null
        };
    },

    dismissInterval: 5000,

    dismissMessage: function () {
        this.setState({message: null});
        this.messageTimeout = null;
    },

    showInfoMessage: function (text) {
        this.dismissInterval = 10000;
        this._showMessage('info', text);
    },

    _showMessage: function (type, text) {
        this.setState({
            message: {
                type: type,
                text: text
            }
        });
        this.messageTimeout = setTimeout(() => this.dismissMessage(), this.dismissInterval);
    },

    showSuccessMessage: function (text) {
        this.dismissInterval = 10000;
        this._showMessage('success', text);
    },

    showErrorMessage: function (text) {
        this.dismissInterval = 10000;
        this._showMessage('danger', text);
    },

    showWarnMessage: function (text) {
        this.dismissInterval = 10000;
        this._showMessage('warning', text);
    },

    showMessage: function (text, msgType) {
        switch (msgType) {
            case Constants.MESSAGE_TYPE.INFO:
                this.showInfoMessage(text);
                break;
            case Constants.MESSAGE_TYPE.ERROR:
                this.showErrorMessage(text);
                break;
            case Constants.MESSAGE_TYPE.SUCCESS:
                this.showSuccessMessage(text);
                break;
            case Constants.MESSAGE_TYPE.WARNING:
                this.showWarnMessage(text);
                break;
            default:
                break;
        }
    },

    renderMessage: function () {
        return this.state.message ? <div className='form-group'>
            <Alert bsStyle={this.state.message.type} onDismiss={this.dismissMessage}>
                <p>{this.state.message.text}</p>
            </Alert>
        </div> : null;
    },

    cleanupMessages: function () {
        if (this.messageTimeout) {
            clearTimeout(this.messageTimeout);
        }
    }
};

module.exports = MessageMixin;
