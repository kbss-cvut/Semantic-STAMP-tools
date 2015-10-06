'use strict';

jest.dontMock('../../js/utils/Utils');

describe('Utility functions tests', function () {

    var Utils = require('../../js/utils/Utils');

    it('Transforms a constant made of text with underscores into text with spaces', function () {
        var constant = 'CONSTANT_WITH_UNDERSCORES';
        var result = Utils.constantToString(constant, true);
        expect(result).toEqual('Constant With Underscores');
    });

    it('Returns the same value when converting to the same unit', function() {
        var value = 117;
        var result = Utils.convertTime('second', 'second', value);
        expect(result).toEqual(value);
    });

    it('Converts minutes to seconds correctly', function() {
       var value = 7;
        var result = Utils.convertTime('minute', 'second', value);
        expect(result).toEqual(7 *  60);
    });

    it('Converts minutes to hours with rounding', function() {
       var value = 7;
        var result = Utils.convertTime('minute', 'hour', value);
        expect(result).toEqual(0);
    });

    it('Converts seconds to minutes with rounding', function() {
        var value = 117;
        var result = Utils.convertTime('second', 'minute', value);
        expect(result).toEqual(2);
    });

    it('Converts seconds to hours with rounding', function() {
       var value = 3600;
        var result = Utils.convertTime('second', 'hour', value);
        expect(result).toEqual(1);
    });

    it('Converts hours to minutes correctly', function() {
        var value = 11;
        var result = Utils.convertTime('hour', 'minute', value);
        expect(result).toEqual(11 * 60);
    });

    it('Converts hours to seconds correctly', function() {
        var value = 11;
        var result = Utils.convertTime('hour', 'second', value);
        expect(result).toEqual(11 * 60 * 60);
    })
});
