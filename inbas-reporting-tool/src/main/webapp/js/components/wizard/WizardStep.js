/**
 * Created by ledvima1 on 11.6.15.
 */

'use strict';

var React = require('react');
var Alert = require('react-bootstrap').Alert;
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;

var WizardStep = React.createClass({

    propTypes: {
        onClose: React.PropTypes.func,
        onFinish: React.PropTypes.func,
        onAdvance: React.PropTypes.func,
        onRetreat: React.PropTypes.func,
        onNext: React.PropTypes.func,
        onPrevious: React.PropTypes.func,
        //component: React.PropTypes.object,
        data: React.PropTypes.object,
        isFirstStep: React.PropTypes.bool,
        isLastStep: React.PropTypes.bool,
        defaultNextDisabled: React.PropTypes.bool
    },

    getInitialState: function () {
        return {
            advanceDisabled: this.props.defaultNextDisabled != null ? this.props.defaultNextDisabled : false,
            retreatDisabled: false
        };
    },
    onAdvance: function (err) {
        if (err) {
            this.setState({
                advanceDisabled: false,
                retreatDisabled: false,
                currentError: err
            });
        } else {
            this.props.onAdvance();
        }
    },
    onNext: function () {
        this.setState({
            advanceDisabled: true,
            retreatDisabled: true
        });
        if (this.props.onNext) {
            this.props.onNext.apply(this, [this.onAdvance]);
        } else {
            this.props.onAdvance();
        }
    },
    onPrevious: function () {
        if (this.props.onPrevious) {
            this.props.onPrevious.apply(this, [this.props.onRetreat]);
        } else {
            this.props.onRetreat();
        }
    },
    onFinish: function () {
        if (this.props.onFinish) {
            this.props.onFinish.apply(this, [this.props.data, this.props.onClose, this.onAdvance]);
        } else {
            this.props.onClose();
        }
    },

    enableNext: function () {
        this.setState({advanceDisabled: false});
    },

    disableNext: function () {
        this.setState({advanceDisabled: true});
    },

    render: function () {
        var previousButton;
        if (!this.props.isFirstStep) {
            previousButton = (<Button onClick={this.onPrevious} disabled={this.state.retreatDisabled} bsStyle='primary' bsSize='small'>Previous</Button>);
        }
        var advanceButton = this.renderAdvanceButton();
        var cancelButton = (<Button onClick={this.props.onClose} bsStyle='primary' bsSize='small'>Cancel</Button>);
        var error = null;
        if (this.state.currentError) {
            error = (<Alert bsStyle='danger'><p>{this.state.currentError.message}</p></Alert>);
        }
        return (
            <div className='wizard-step'>
                <div className='wizard-step-content'>
                    {this.renderComponent()}
                </div>
                <ButtonToolbar style={{float: 'right'}}>
                    {previousButton}
                    {advanceButton}
                    {cancelButton}
                </ButtonToolbar>
                {error}
            </div>
        );
    },

    renderAdvanceButton: function () {
        var disabledTitle = this.state.advanceDisabled ? 'Some required values are missing' : null;
        var button;
        if (!this.props.isLastStep) {
            button = (
                <Button onClick={this.onNext} disabled={this.state.advanceDisabled} bsStyle='primary' bsSize='small'
                        title={disabledTitle}>Next</Button>);
        } else {
            button = (<Button onClick={this.onFinish} disabled={this.state.advanceDisabled} bsStyle='primary' bsSize='small'
                              title={disabledTitle}>Finish</Button>);
        }
        return button;
    },

    renderComponent: function() {
        return React.createElement(this.props.component, {
            data: this.props.data,
            enableNext: this.enableNext,
            disableNext: this.disableNext,
            next: this.onNext,
            previous: this.onPrevious,
            finish: this.onFinish,
            insertStepAfterCurrent: this.props.onInsertStepAfterCurrent,
            addStep: this.props.onAddStep,
            removeStep: this.props.onRemoveStep
        });
    }
});

module.exports = WizardStep;
