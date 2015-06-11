/**
 * Created by ledvima1 on 11.6.15.
 */

'use strict';

var React = require('react');
var Alert = require('react-bootstrap').Alert;
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;

var WizardStep = React.createClass({
    getInitialState: function () {
        return {
            advanceDisabled: false,
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
        this.props.onNext.apply(this, [this.onAdvance]);
    },
    onPrevious: function () {
        if (this.props.onPrevious) {
            this.props.onPrevious.apply(this, [this.props.onRetreat]);
        } else {
            this.props.onRetreat();
        }
    },
    onFinish: function() {
        if (this.props.onFinish) {
            this.props.onFinish.apply(this, [this.props.data, this.props.onClose, this.onAdvance]);
        } else {
            this.props.onClose();
        }
    },

    render: function () {
        var previousButton;
        if (!this.props.isFirstStep) {
            previousButton = (<Button onClick={this.onPrevious} disabled={this.state.retreatDisabled} bsStyle="primary">Previous</Button>);
        }
        var nextButton;
        var finishButton;
        if (!this.props.isLastStep) {
            nextButton = (
                <Button onClick={this.onNext} disabled={this.state.advanceDisabled} bsStyle="primary">Next</Button>);
        } else {
            finishButton = (<Button onClick={this.onFinish} bsStyle="primary">Finish</Button>);
        }
        var cancelButton = (<Button onClick={this.props.onClose} bsStyle="primary">Cancel</Button>);
        var error;
        if (this.state.currentError) {
            error = (<Alert bsStyle="danger"><p>{this.state.currentError.message}</p></Alert>);
        }
        var Component = this.props.component;
        return (
            <div className="wizard-step">
                <Component data={this.props.data}/>
                <ButtonToolbar>
                    {previousButton}
                    {nextButton}
                    {finishButton}
                    {cancelButton}
                </ButtonToolbar>
                {error}
            </div>
        )
    }
});

module.exports = WizardStep;
