'use strict';

describe('Question answer processor', () => {

    var Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        Vocabulary = require('../../js/constants/Vocabulary'),
        QuestionAnswerProcessor = require('../../js/model/QuestionAnswerProcessor').default;

    it('transforms answers for a question', () => {
        var question = {},
            result;
        generateAnswers(question);
        result = QuestionAnswerProcessor.processQuestionAnswerHierarchy(question);
        verifyAnswers(question, result);
    });

    function generateAnswers(question) {
        question[Constants.FORM.HAS_ANSWER] = [];
        for (var i = 0, cnt = Generator.getRandomPositiveInt(1, 5); i < cnt; i++) {
            var answer = {};
            answer['@id'] = Generator.getRandomUri();
            answer['@value'] = i;
            question[Constants.FORM.HAS_ANSWER].push(answer);
        }
    }

    function verifyAnswers(expectedQuestion, actualQuestion) {
        if (!expectedQuestion[Constants.FORM.HAS_ANSWER]) {
            return;
        }
        expect(actualQuestion.answers).toBeDefined();
        expect(actualQuestion.answers.length).toEqual(expectedQuestion[Constants.FORM.HAS_ANSWER].length);
        for (var i = 0, len = actualQuestion.answers.length; i < len; i++) {
            expect(actualQuestion.answers[i].textValue).toEqual(expectedQuestion[Constants.FORM.HAS_ANSWER][i]['@value']);
            expect(actualQuestion.answers[i].uri).toEqual(expectedQuestion[Constants.FORM.HAS_ANSWER][i]['@id']);
        }
    }

    it('transforms hierarchy of questions and answers', () => {
        var question = generateQuestions(),
            result;
        result = QuestionAnswerProcessor.processQuestionAnswerHierarchy(question);
        
        verifyQuestions(question, result);
    });

    function generateQuestions() {
        var question = {};
        question['@id'] = Generator.getRandomUri();
        question[Vocabulary.RDFS_LABEL] = 'Test0';
        question[Vocabulary.RDFS_COMMENT] = 'Test0 Comment';
        question[Constants.FORM.HAS_SUBQUESTION] = [];
        for (var i = 0, cnt = Generator.getRandomPositiveInt(1, 5); i < cnt; i++) {
            question[Constants.FORM.HAS_SUBQUESTION].push(generateSubQuestions(0, 5));        
        }
        return question;
    }
    
    function generateSubQuestions(depth, maxDepth) {
        var question = {};
        question['@id'] = Generator.getRandomUri();
        question[Vocabulary.RDFS_LABEL] = 'Test' + Generator.getRandomInt();
        question[Vocabulary.RDFS_COMMENT] = 'Test Comment';
        if (depth < maxDepth) {
            question[Constants.FORM.HAS_SUBQUESTION] = [];
            for (var i = 0, cnt = Generator.getRandomPositiveInt(1, 5); i < cnt; i++) {
                question[Constants.FORM.HAS_SUBQUESTION].push(generateSubQuestions(depth + 1, maxDepth));
            }
        }
        generateAnswers(question);
        return question;
    }
    
    function verifyQuestions(expected, actual) {
        expect(actual.types.indexOf(expected['@id'])).not.toEqual(-1);
        verifyAnswers(expected, actual);
        if (expected[Constants.FORM.HAS_SUBQUESTION]) {
            expect(actual.subQuestions).toBeDefined();
            expect(actual.subQuestions.length).toEqual(expected[Constants.FORM.HAS_SUBQUESTION].length);
            for (var i = 0, len = actual.subQuestions.length; i < len; i++) {
                verifyQuestions(expected[Constants.FORM.HAS_SUBQUESTION][i], actual.subQuestions[i]);
            }
        }
    }
});
