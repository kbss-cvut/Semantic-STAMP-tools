'use strict';

var React = require('react');
var ReactDOM = require('react-dom');
var TestUtils = require('react-addons-test-utils');
var rewire = require('rewire');

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
    },

    /**
     * Finds component with the specified text.
     * @param root Root of the tree where the component is searched for
     * @param component Component class
     * @param text Component text
     */
    getComponentByText: function (root, component, text) {
        var components = TestUtils.scryRenderedComponentsWithType(root, component);
        return this._getNodeByText(components, text);
    },

    _getNodeByText: function (components, text) {
        for (var i = 0, len = components.length; i < len; i++) {
            var node = ReactDOM.findDOMNode(components[i]);
            if (node.textContent === text) {
                return node;
            }
        }
        return null;
    },

    /**
     * Finds component with the specified text.
     *
     * This version searches components by tag, so it will catch also simple components like div.
     * @param root Root of the tree where the component is searched for
     * @param tag Tag name
     * @param text Component text
     */
    getComponentByTagAndText: function (root, tag, text) {
        var components = TestUtils.scryRenderedDOMComponentsWithTag(root, tag);
        return this._getNodeByText(components, text);
    },

    mockFactors: function (investigation) {
        var Factors = rewire('../../js/components/investigation/Factors'),
            GanttController = jasmine.createSpyObj('GanttController', ['init', 'setScale', 'expandSubtree', 'updateOccurrenceEvent']),
            FactorRenderer = jasmine.createSpyObj('FactorRenderer', ['renderFactors']),
            FactorJsonSerializer = jasmine.createSpyObj('FactorJsonSerializer', ['getFactorHierarchy', 'getLinks', 'setGanttController']);
        Factors.__set__('GanttController', GanttController);
        Factors.__set__('FactorRenderer', FactorRenderer);
        Factors.__set__('FactorJsonSerializer', FactorJsonSerializer);
        investigation.__set__('Factors', Factors);
    }
};
