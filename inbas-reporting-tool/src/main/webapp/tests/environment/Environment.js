'use strict';

var React = require('react');
var TestUtils = require('react-addons-test-utils');

var TestApp = require('./TestApp');

module.exports = {

    /**
     * Renders the specified component, wrapping it in a TestApp instance, which is used to initialize some required
     * context data, e.g. i18n.
     * @param component Component to render
     * @return {*|!ReactComponent} The rendered component
     */
    render: function (component) {
        var type = component.type,
            result = TestUtils.renderIntoDocument(<TestApp>{component}</TestApp>),
            renderedComponent = TestUtils.findRenderedComponentWithType(result, type);
        //console.log(renderedComponent);
        if (renderedComponent.refs && renderedComponent.refs.wrappedElement) {
            return renderedComponent.refs.wrappedElement;
        } else {
            return renderedComponent;
        }
    }
};
