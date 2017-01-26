'use strict';

import React from "react";
import Mask from "../../Mask";

/**
 * Higher order component which provides loading mask facilities to the wrapped component.
 * @param Component The component to wrap
 * @param options Allows to specify what container tag should be used. Default is 'div'
 */
const LoadingWrapper = (Component, options) => class extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
            msg: undefined
        };
    }

    getWrappedComponent() {
        // Enable composition of multiple HOCs.
        return this._wrappedComponent && this._wrappedComponent.getWrappedComponent ?
            this._wrappedComponent.getWrappedComponent() : this._wrappedComponent;
    }

    loadingOn = (msg) => {
        this.setState({loading: true, msg: msg});
    };

    loadingOff = () => {
        this.setState({loading: false, msg: undefined});
    };

    render() {
        const tag = options && options.tag ? options.tag : 'div';
        return React.createElement(tag, null,
            this.state.loading && <Mask text={this.state.msg}/>,
            <Component ref={(c) => this._wrappedComponent = c} loadingOn={this.loadingOn}
                       loadingOff={this.loadingOff} {...this.props}/>
        );
    }
};

export default LoadingWrapper;
