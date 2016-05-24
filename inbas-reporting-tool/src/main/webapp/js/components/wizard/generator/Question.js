'use strict';

import React from "react";
import assign from "object-assign";
import {Panel} from "react-bootstrap";
import Answer from "./Answer";
import Constants from "../../../constants/Constants";
import QuestionAnswerProcessor from "../../../model/QuestionAnswerProcessor";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

export default class Question extends React.Component {
    static propTypes = {
        question: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        index: React.PropTypes.number,
        withoutPanel: React.PropTypes.bool
    };

    constructor(props) {
        super(props);
    }

    onAnswerChange = (answerIndex, change) => {
        this._onChange(Constants.HAS_ANSWER, answerIndex, change);
    };

    onSubQuestionChange = (subQuestionIndex, change) => {
        this._onChange(Constants.HAS_SUBQUESTION, subQuestionIndex, change);
    };

    _onChange(att, valueIndex, newValue) {
        var newState = assign({}, this.props.question);
        newState[att][valueIndex] = newValue;
        this.props.onChange(newState);
    }

    render() {
        if (Question._isSection(this.props.question)) {
            if (this.props.withoutPanel) {
                return <div>
                    {this.renderAnswers()}
                    {this.renderSubQuestions()}
                </div>;
            } else {
                var label = this.props.question[Vocabulary.RDFS_LABEL];
                return <Panel header={<h5>{label}</h5>} bsStyle='info'>
                    {this.renderAnswers()}
                    {this.renderSubQuestions()}
                </Panel>;
            }
        } else {
            return <div>{this.renderAnswers()}</div>;
        }
    }

    static _isSection(question) {
        return Utils.hasValue(question, Constants.LAYOUT_CLASS, Constants.QUESTION_SECTION);
    }

    renderAnswers() {
        var question = this.props.question,
            children = [], row = [];
        var answers = this._getAnswers();
        for (var i = 0, len = answers.length; i < len; i++) {
            row.push(<Answer key={'row-item-' + i} index={i} answer={answers[i]} question={question}
                             onChange={this.onAnswerChange}/>);
            if (row.length === Constants.GENERATED_ROW_SIZE) {
                children.push(<div className='row' key={'question-row-' + i}>{row}</div>);
                row = [];
            }
        }
        if (row.length > 0) {
            children.push(<div className='row' key={'question-row-' + i}>{row}</div>);
        }
        return children;
    }

    _getAnswers() {
        var question = this.props.question;
        if (!question[Constants.HAS_ANSWER]) {
            if (Question._isSection(question)) {
                question[Constants.HAS_ANSWER] = [];
            } else {
                question[Constants.HAS_ANSWER] = [QuestionAnswerProcessor.generateAnswer(question)];
            }
        }
        if (!Array.isArray(question[Constants.HAS_ANSWER])) {
            question[Constants.HAS_ANSWER] = [question[Constants.HAS_ANSWER]];
        }
        return question[Constants.HAS_ANSWER];
    }

    renderSubQuestions() {
        var children = [],
            subQuestions = this._getSubQuestions();
        for (var i = 0, len = subQuestions.length; i < len; i++) {
            children.push(<Question key={'sub-question-' + i} index={i} question={subQuestions[i]}
                                    onChange={this.onSubQuestionChange}/>);
        }
        return children;
    }

    _getSubQuestions() {
        var question = this.props.question;
        if (!question[Constants.HAS_SUBQUESTION]) {
            question[Constants.HAS_SUBQUESTION] = [];
        }
        if (!Array.isArray(question[Constants.HAS_SUBQUESTION])) {
            question[Constants.HAS_SUBQUESTION] = [question[Constants.HAS_SUBQUESTION]];
        }
        return question[Constants.HAS_SUBQUESTION];
    }
}
