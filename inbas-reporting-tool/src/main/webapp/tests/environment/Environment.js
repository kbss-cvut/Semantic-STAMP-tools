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
        if (renderedComponent.getWrappedInstance) {
            return renderedComponent.getWrappedInstance();
        } else {
            return renderedComponent;
        }
    },

    /**
     * Renders the specified component into a table body, wrapping it all in a TestApp instance, which is used to
     * initialize some required context data, e.g. i18n.
     *
     * This is useful for components representing table rows.
     * @param component Component to render
     * @return {*|!ReactComponent} The rendered component
     */
    renderIntoTable: function (component) {
        var type = component.type,
            result = TestUtils.renderIntoDocument(<TestApp>
                <table>
                    <tbody>{component}</tbody>
                </table>
            </TestApp>),
            renderedComponent = TestUtils.findRenderedComponentWithType(result, type);
        //console.log(renderedComponent);
        if (renderedComponent.getWrappedInstance) {
            return renderedComponent.getWrappedInstance();
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
        return this._getNodeByText(components, text, true);
    },

    _getNodeByText: function (components, text, strict) {
        for (var i = 0, len = components.length; i < len; i++) {
            var node = ReactDOM.findDOMNode(components[i]);
            if (strict && node.textContent === text) {
                return node;
            } else if (!strict && node.textContent.indexOf(text) !== -1) {
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
        return this._getNodeByText(components, text, true);
    },

    /**
     * Finds component which contains the specified text.
     *
     * This version searches components by tag, so it will catch also simple components like div.
     * @param root Root of the tree where the component is searched for
     * @param tag Tag name
     * @param text Text contained in the component's text content
     */
    getComponentByTagAndContainedText: function (root, tag, text) {
        var components = TestUtils.scryRenderedDOMComponentsWithTag(root, tag);
        return this._getNodeByText(components, text, false);
    },

    mockFactors: function (investigation) {
        var Factors = rewire('../../js/components/factor/Factors'),
            GanttController = jasmine.createSpyObj('GanttController', ['init', 'setScale', 'expandSubtree', 'updateOccurrenceEvent']),
            FactorRenderer = jasmine.createSpyObj('FactorRenderer', ['renderFactors']),
            FactorJsonSerializer = jasmine.createSpyObj('FactorJsonSerializer', ['getFactorGraph', 'getFactorHierarchy', 'getLinks', 'setGanttController']);
        Factors.__set__('GanttController', GanttController);
        Factors.__set__('FactorRenderer', FactorRenderer);
        Factors.__set__('FactorJsonSerializer', FactorJsonSerializer);
        investigation.__set__('Factors', Factors);
    },

    /**
     * Creates a mock objects with the specified methods.
     *
     * The methods adhere to the builder pattern - they return the mock instance itself.
     * @param reqMockMethods array of methods to mock
     */
    mockRequestMethods: function (reqMockMethods) {
        var reqMock = jasmine.createSpyObj('request', reqMockMethods);
        for (var i = 0; i < reqMockMethods.length; i++) {
            // All mock methods just return the instance to adhere to the builder pattern implemented by request
            reqMock[reqMockMethods[i]].and.callFake(function () {
                return reqMock;
            });
        }
        return reqMock;
    },

    /**
     * Mocks the Logger, so that test console output is not polluted with log messages.
     */
    mockLogger: function () {
        return jasmine.createSpyObj('Logger', ['warn', 'log', 'error']);
    },

    /**
     * Mocks the Gantt component and sets in as global variable to simulate its ordinary behaviour.
     *
     * Returns the mock for possible further use.
     */
    mockGantt: function () {
        var gantt = jasmine.createSpyObj('gantt', ['init', 'open', 'addTask', 'addLink', 'getChildren', 'attachEvent', 'clearAll', 'render', 'calculateDuration']);
        gantt.config = {
            links: {}
        };
        gantt.templates = {};
        gantt.date = {};
        gantt.getChildren.and.callFake(() => {
            return [];
        });
        jasmine.getGlobal().gantt = gantt;
        return gantt;
    }
};
