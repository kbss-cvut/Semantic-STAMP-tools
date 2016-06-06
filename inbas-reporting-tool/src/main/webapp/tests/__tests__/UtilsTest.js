'use strict';

describe('Utility functions tests', function () {

    var Utils = require('../../js/utils/Utils'),
        Vocabulary = require('../../js/constants/Vocabulary'),
        Generator = require('../environment/Generator').default;

    it('Transforms a constant made of text with underscores into text with spaces', function () {
        var constant = 'CONSTANT_WITH_UNDERSCORES';
        var result = Utils.constantToString(constant, true);
        expect(result).toEqual('Constant With Underscores');
    });

    it('Returns the same value when converting to the same unit', function () {
        var value = 117;
        var result = Utils.convertTime('second', 'second', value);
        expect(result).toEqual(value);
    });

    it('Converts minutes to seconds correctly', function () {
        var value = 7;
        var result = Utils.convertTime('minute', 'second', value);
        expect(result).toEqual(7 * 60);
    });

    it('Converts minutes to hours with rounding', function () {
        var value = 7;
        var result = Utils.convertTime('minute', 'hour', value);
        expect(result).toEqual(0);
    });

    it('Converts seconds to minutes with rounding', function () {
        var value = 117;
        var result = Utils.convertTime('second', 'minute', value);
        expect(result).toEqual(2);
    });

    it('Converts seconds to hours with rounding', function () {
        var value = 3600;
        var result = Utils.convertTime('second', 'hour', value);
        expect(result).toEqual(1);
    });

    it('Converts hours to minutes correctly', function () {
        var value = 11;
        var result = Utils.convertTime('hour', 'minute', value);
        expect(result).toEqual(11 * 60);
    });

    it('Converts hours to seconds correctly', function () {
        var value = 11;
        var result = Utils.convertTime('hour', 'second', value);
        expect(result).toEqual(11 * 60 * 60);
    });

    it('Extracts path from unparametrized location', function () {
        jasmine.getGlobal().window = {
            location: {
                hash: '#/reports?_k=3123123'
            }
        };
        expect(Utils.getPathFromLocation()).toEqual('reports');
    });

    it('Extracts path from unparametrized location without slash after hashtag', function () {
        jasmine.getGlobal().window = {
            location: {
                hash: '#login?_k=3123123'
            }
        };
        expect(Utils.getPathFromLocation()).toEqual('login');
    });

    it('Extracts path from parametrized location', function () {
        jasmine.getGlobal().window = {
            location: {
                hash: '#/reports/1234567890?_k=3123123'
            }
        };
        expect(Utils.getPathFromLocation()).toEqual('reports/1234567890');
    });

    it('transforms JSON-LD input into Typeahead-friendly format', () => {
        var jsonLd = Generator.getJsonLdSample(),
            result = Utils.processTypeaheadOptions(jsonLd);
        expect(result.length).toEqual(jsonLd.length);
        for (var i = 0, len = jsonLd.length; i < len; i++) {
            expect(result[i].id).toEqual(jsonLd[i]['@id']);
            expect(result[i].type).toEqual(jsonLd[i]['@type']);
            expect(result[i].name).toEqual(jsonLd[i][Vocabulary.RDFS_LABEL]);
            if (jsonLd[i][Vocabulary.RDFS_COMMENT]) {
                expect(result[i].description).toEqual(jsonLd[i][Vocabulary.RDFS_COMMENT]);
            }
        }
    });

    it('handles transformation of an empty array', () => {
        var result = Utils.processTypeaheadOptions([]);
        expect(result).toEqual([]);
    });

    it('handles transformation of null/undefined', () => {
        var result = Utils.processTypeaheadOptions(null);
        expect(result).toEqual([]);
        result = Utils.processTypeaheadOptions();
        expect(result).toEqual([]);
    });

    describe('getJsonAttValue', () => {
        it('extracts value of a JSON literal value', () => {
            var a = 'a',
                b = true,
                c = 12345,
                d = 'Label',
                obj = {
                    'a': a,
                    'b': b,
                    'c': c
                };
            obj[Vocabulary.RDFS_LABEL] = d;
            expect(Utils.getJsonAttValue(obj, 'a')).toEqual(a);
            expect(Utils.getJsonAttValue(obj, 'b')).toEqual(b);
            expect(Utils.getJsonAttValue(obj, 'c')).toEqual(c);
            expect(Utils.getJsonAttValue(obj, Vocabulary.RDFS_LABEL)).toEqual(d);
        });

        it('extracts value from a JSON value object with tag', () => {
            var label = 'Label',
                obj = {};
            obj[Vocabulary.RDFS_LABEL] = {
                '@language': 'en',
                '@value': label
            };
            expect(Utils.getJsonAttValue(obj, Vocabulary.RDFS_LABEL)).toEqual(label);
        });

        it('returns null if the attribute is not present', () => {
            var obj = {};
            obj[Vocabulary.RDFS_LABEL] = {
                '@language': 'en',
                '@value': 'Label'
            };
            expect(Utils.getJsonAttValue(obj, Vocabulary.RDFS_COMMENT)).toBeNull();
        });
    });
});
