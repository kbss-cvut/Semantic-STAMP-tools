'use strict';

import React from "react";
import assign from "object-assign";
import {Panel} from "react-bootstrap";
import Answer from "./Answer";
import Constants from "../../../constants/Constants";
import QuestionAnswerProcessor from "../../../model/QuestionAnswerProcessor";
import Vocabulary from "../../../constants/Vocabulary";

export default class Question extends React.Component {
    static propTypes = {
        question: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        index: React.PropTypes.number
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
        var label = this.props.question[Vocabulary.RDFS_LABEL];
        return <Panel title={<h3>{label}</h3>} bsStyle='info'>
            {this.renderAnswers()}
            {this.renderSubQuestions()}
        </Panel>;
    }

    renderAnswers() {
        var question = this.props.question,
            children = [], row = [];
        if (!question[Constants.HAS_ANSWER]) {
            question[Constants.HAS_ANSWER] = [QuestionAnswerProcessor.generateAnswer(question)];
        }
        var answers = question[Constants.HAS_ANSWER];
        // TODO It should always be an array (to conform to the object model)
        if (!Array.isArray(answers)) {
            children.push(<div className='row' key={'question-row-0'}>
                <Answer index={0} answer={answers} question={question} onChange={this.onAnswerChange}/>
            </div>);
        } else {
            for (var i = 0, len = answers.length; i < len; i++) {
                row.push(<Answer index={i} answer={answers[i]} question={question} onChange={this.onAnswerChange}/>);
                if (row.length === Constants.GENERATED_ROW_SIZE) {
                    children.push(<div className='row' key={'question-row-' + i}>{row}</div>);
                    row = [];
                }
            }
            if (row.length > 0) {
                children.push(<div className='row' key={'question-row-' + i}>{row}</div>);
            }
        }
        return children;
    }

    renderSubQuestions() {
        var children = [],
            question = this.props.question;
        if (!question[Constants.HAS_SUBQUESTION]) {
            return children;
        }
        var subQuestions = question[Constants.HAS_SUBQUESTION];
        // TODO It should always be an array (to conform to the object model)
        if (!Array.isArray(subQuestions)) {
            children.push(<Question index={0} question={subQuestions} onChange={this.onSubQuestionChange}/>);
        } else {
            for (var i = 0, len = subQuestions.length; i < len; i++) {
                children.push(<Question index={i} question={subQuestions[i]} onChange={this.onSubQuestionChange}/>);
            }
        }
        return children;
    }
}
