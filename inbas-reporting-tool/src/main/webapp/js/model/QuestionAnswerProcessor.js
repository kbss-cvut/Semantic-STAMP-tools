'use strict';

import Constants from "../constants/Constants";
import Utils from "../utils/Utils";

export default class QuestionAnswerProcessor {

    /**
     * Transforms the QA hierarchy from JSON-LD-based structure to the object model-based one.
     * @param rootQuestion
     */
    static processQuestionAnswerHierarchy(rootQuestion) {
        if (!rootQuestion) {
            return null;
        }
        return QuestionAnswerProcessor._processQuestion(rootQuestion);
    }

    static _processQuestion(question) {
        var result = {},
            i, len;
        result.id = question['@id'];
        result.origin = Utils.getJsonAttValue(question, Constants.FORM.HAS_QUESTION_ORIGIN, '@id');
        if (question[Constants.FORM.HAS_SUBQUESTION]) {
            result.subQuestions = [];
            for (i = 0, len = question[Constants.FORM.HAS_SUBQUESTION].length; i < len; i++) {
                result.subQuestions.push(QuestionAnswerProcessor._processQuestion(question[Constants.FORM.HAS_SUBQUESTION][i]));
            }
        }
        if (question[Constants.FORM.HAS_ANSWER]) {
            result.answers = [];
            if (!Array.isArray(question[Constants.FORM.HAS_ANSWER])) {
                question[Constants.FORM.HAS_ANSWER] = [question[Constants.FORM.HAS_ANSWER]];
            }
            for (i = 0, len = question[Constants.FORM.HAS_ANSWER].length; i < len; i++) {
                result.answers.push(QuestionAnswerProcessor.processAnswer(question[Constants.FORM.HAS_ANSWER][i]));
            }
        }
        return result;
    }

    static processAnswer(answer) {
        var result = {};
        result.uri = answer['@id'];
        result.origin = Utils.getJsonAttValue(answer, Constants.FORM.HAS_ANSWER_ORIGIN, '@id');
        if (answer[Constants.FORM.HAS_OBJECT_VALUE]) {
            result.codeValue = Utils.getJsonAttValue(answer,Constants.FORM.HAS_OBJECT_VALUE, '@id');
        } else {
            result.textValue = Utils.getJsonAttValue(answer, Constants.FORM.HAS_DATA_VALUE);
        }
        return result;
    }

    /**
     * Generates an empty answer for the specified question
     * @param question
     */
    static generateAnswer(question) {
        var answer = {};
        answer[Constants.FORM.HAS_DATA_VALUE] = {
            '@value': ''
        };
        return answer;
    }
}
