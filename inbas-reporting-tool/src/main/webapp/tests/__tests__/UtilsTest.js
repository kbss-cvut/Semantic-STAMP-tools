'use strict';

jest.dontMock('../../js/utils/Utils');

describe('Utility functions tests', function () {

    var React = require('react'),
        Utils = require('../../js/utils/Utils');

    it('Transforms a constant made of text with underscores into text with spaces', function () {
        var constant = 'CONSTANT_WITH_UNDERSCORES';
        var result = Utils.constantToString(constant, true);
        expect(result).toEqual('Constant With Underscores');
    });
});