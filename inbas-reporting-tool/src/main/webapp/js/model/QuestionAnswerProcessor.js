'use strict';

import Constants from "../constants/Constants";

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
        result.types = [question['@id']];
        if (question[Constants.HAS_SUBQUESTION]) {
            result.subQuestions = [];
            for (i = 0, len = question[Constants.HAS_SUBQUESTION].length; i < len; i++) {
                result.subQuestions.push(QuestionAnswerProcessor._processQuestion(question[Constants.HAS_SUBQUESTION][i]));
            }
        }
        if (question[Constants.HAS_ANSWER]) {
            result.answers = [];
            for (i = 0, len = question[Constants.HAS_ANSWER].length; i < len; i++) {
                result.answers.push(QuestionAnswerProcessor.processAnswer(question[Constants.HAS_ANSWER][i], question));
            }
        }
        return result;
    }

    static processAnswer(answer, question) {
        var result = {};
        // TODO
        if (question['code-answer']) {
            result.codeValue = answer.value;
        } else {
            result.textValue = answer.value;
        }
        return result;
    }
}
