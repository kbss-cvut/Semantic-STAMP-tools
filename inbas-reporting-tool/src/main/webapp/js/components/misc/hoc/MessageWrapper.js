'use strict';

import React from "react";
import {Alert} from "react-bootstrap";
import Constants from "../../../constants/Constants";

/**
 * Decorates the specified component with the ability to display messages.
 * @param Component The component to decorate
 * @param options Options, allow to specify in what element all should be wrapped. It is a 'div' by default.
 */
const MessageWrapper = (Component, options) => class extends React.Component {
    constructor(props) {
        super(props);
        this.dismissInterval = Constants.MESSAGE_DURATION;
        this.state = {
            message: null
        };
    }

    componentWillUnmount() {
        if (this.messageTimeout) {
            clearTimeout(this.messageTimeout);
        }
    }

    _dismissMessage = () => {
        this.setState({message: null});
        this.messageTimeout = null;
    };

    showInfoMessage = (text, interval = Constants.MESSAGE_DURATION) => {
        this.dismissInterval = interval;
        this._showMessage(Constants.MESSAGE_TYPE.INFO, text);
    };

    _showMessage(type, text) {
        this.setState({
            message: {
                type: type,
                text: text
            }
        });
        this.messageTimeout = setTimeout(() => this._dismissMessage(), this.dismissInterval);
    }

    showSuccessMessage = (text, interval = Constants.MESSAGE_DURATION) => {
        this.dismissInterval = interval;
        this._showMessage(Constants.MESSAGE_TYPE.SUCCESS, text);
    };

    showErrorMessage = (text, interval = Constants.MESSAGE_DURATION) => {
        this.dismissInterval = interval;
        this._showMessage(Constants.MESSAGE_TYPE.ERROR, text);
    };

    showWarnMessage = (text, interval = Constants.MESSAGE_DURATION) => {
        this.dismissInterval = interval;
        this._showMessage(Constants.MESSAGE_TYPE.WARNING, text);
    };

    render() {
        const tag = options && options.tag ? options.tag : 'div',
            methods = {
                showInfoMessage: this.showInfoMessage,
                showSuccessMessage: this.showSuccessMessage,
                showErrorMessage: this.showErrorMessage,
                showWarnMessage: this.showWarnMessage
            };
        return React.createElement(tag, null,
            <Component ref={(c) => this._wrappedComponent = c} {...methods} {...this.props}/>,
            this.state.message && <div className='message-container'>
                <Alert bsStyle={this.state.message.type} onDismiss={this._dismissMessage}>
                    <p>{this.state.message.text}</p>
                </Alert>
            </div>
        );
    }
};

export default MessageWrapper;
