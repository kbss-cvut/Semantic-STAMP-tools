/**
 * @jsx
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var ListGroup = require('react-bootstrap').ListGroup;
var ListGroupItem = require('react-bootstrap').ListGroupItem;

var WizardStep = require('./WizardStep');

var Wizard = React.createClass({

    propTypes: {
        start: React.PropTypes.number,
        steps: React.PropTypes.array
    },

    getInitialState: function () {
        var data = assign({}, this.props);
        return {
            currentStep: this.props.start || 0,
            data: data,
            stepData: [],
            nextDisabled: false,
            previousDisabled: false
        };
    },

    getDefaultProps: function () {
        return {
            steps: []
        };
    },

    onAdvance: function (dataCallback) {
        if (this.state.currentStep === this.props.steps.length - 1) {
            return;
        }
        this.props.steps[this.state.currentStep + 1].visited = true;
        var stepData = this.state.stepData.slice();
        stepData[this.state.currentStep] = dataCallback();
        this.setState({
            currentStep: this.state.currentStep + 1,
            stepData: stepData
        });
    },

    onRetreat: function () {
        if (this.state.currentStep === 0) {
            return;
        }
        this.setState({
            currentStep: this.state.currentStep - 1
        });
    },

    onFinish: function (errCallback) {
        var data = {
            data: this.state.data,
            stepData: this.state.stepData
        };
        this.props.onFinish(data, this.props.onClose, errCallback);
    },

    /**
     * Insert the specified step after the current one.
     * @param step The step to insert
     */
    onInsertStepAfterCurrent: function (step) {
        this.props.steps.splice(this.state.currentStep + 1, 0, step);
    },

    /**
     * Adds the specified step to the end of this wizard.
     * @param step The step to add
     */
    onAddStep: function (step) {
        this.props.steps.push(step);
    },

    onRemoveStep: function (stepId) {
        var stateUpdate = {};
        for (var i = 0, len = this.props.steps.length; i < len; i++) {
            if (this.props.steps[i].id === stepId) {
                this.props.steps.splice(i, 1);
                if (i === this.state.currentStep) {
                    stateUpdate.currentStep = this.state.currentStep - 1;
                }
                break;
            }
        }
        this.setState(stateUpdate);
    },


    render: function () {
        var navMenu = this.initNavMenu();
        var component = this.initComponent();
        return (
            <div className="wizard">
                <div className="wizard-nav col-xs-2">
                    <ListGroup>
                        {navMenu}
                    </ListGroup>
                </div>
                <div className="wizard-content col-xs-10">
                    {component}
                </div>
            </div>
        );
    },

    initNavMenu: function () {
        return this.props.steps.map(function (step, index) {
            return (<ListGroupItem key={'nav' + index} onClick={this.navigate} id={'wizard-nav-' + index}
                                   active={index === this.state.currentStep ? 'active' : ''}>{step.name}</ListGroupItem>);
        }.bind(this));
    },

    navigate: function (e) {
        var item = e.target;
        var index = Number(item.id.substring('wizard-nav-'.length));

        if (index === this.state.currentStep || index >= this.props.steps.length) {
            return;
        }
        // Can we jump forward?
        if (index > this.state.currentStep && !this.props.steps[index].visited && !this.props.enableForwardSkip) {
            return;
        }
        this.setState({
            currentStep: index
        });
    },

    initComponent: function () {
        var step = this.props.steps[this.state.currentStep];

        return React.createElement(WizardStep, {
            key: 'step' + this.state.currentStep,
            onClose: this.props.onClose,
            onFinish: this.onFinish,
            onAdvance: this.onAdvance,
            onRetreat: this.onRetreat,
            onNext: step.onNext,
            onPrevious: step.onPrevious,
            onInsertStepAfterCurrent: this.onInsertStepAfterCurrent,
            onAddStep: this.onAddStep,
            onRemoveStep: this.onRemoveStep,
            component: step.component,
            title: step.name,
            data: this.state.data,
            stepData: step.data,
            isFirstStep: this.state.currentStep === 0,
            isLastStep: this.state.currentStep === this.props.steps.length - 1,
            defaultNextDisabled: step.defaultNextDisabled
        });
    }
});

module.exports = Wizard;
