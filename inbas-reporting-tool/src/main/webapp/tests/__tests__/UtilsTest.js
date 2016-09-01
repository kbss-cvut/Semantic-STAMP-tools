'use strict';

describe('Utility functions tests', function () {

    var Utils = require('../../js/utils/Utils'),
        Constants = require('../../js/constants/Constants'),
        Vocabulary = require('../../js/constants/Vocabulary'),
        Generator = require('../environment/Generator').default;

    describe('formatDate', () => {
        it('returns emtpy string when no date is specified', () => {
            expect(Utils.formatDate(null)).toEqual('');
        });
    });

    it('Transforms a constant with known preposition/auxiliary word into text with spaces and correctly capitalized words', function () {
        expect(Utils.constantToString('BARRIER_NOT_EFFECTIVE', true)).toEqual('Barrier not Effective');
        expect(Utils.constantToString('NOT_EFFECTIVE', true)).toEqual('Not Effective');
        expect(Utils.constantToString('PLANE_WITHOUT_WINGS', true)).toEqual('Plane without Wings');
        expect(Utils.constantToString('CONSTANT_WITH_UNDERSCORES', true)).toEqual('Constant with Underscores');
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

    describe('addParametersToUrl', () => {

        it('adds parameters to URL', () => {
            var url = '/rest/formGen',
                parameters = {
                    pOne: '12345',
                    pTwo: '54321'
                },

                result = Utils.addParametersToUrl(url, parameters);
            expect(result.indexOf('pOne=' + parameters.pOne)).not.toEqual(-1);
            expect(result.indexOf('pTwo=' + parameters.pTwo)).not.toEqual(-1);
        });

        it('adds parameters to URL which already contains query string', () => {
            var url = '/rest/formGen?paramZero=0',
                parameters = {
                    pOne: '12345',
                    pTwo: '54321'
                },

                result = Utils.addParametersToUrl(url, parameters);
            expect(result.indexOf('&pOne=' + parameters.pOne)).not.toEqual(-1);
            expect(result.indexOf('&pTwo=' + parameters.pTwo)).not.toEqual(-1);
        });
    });

    describe('determineTimeScale', () => {
        it('returns seconds for small time scale', () => {
            var startTime = Date.now();
            var root = {
                startTime: startTime,
                endTime: startTime + 50 * 1000
            };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
            root.endTime = startTime;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 1000 - 1;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
        });

        it('returns minutes for medium time scale', () => {
            var startTime = Date.now();
            var root = {
                startTime: startTime,
                endTime: startTime + 10 * 60 * 1000
            };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.MINUTE);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 1000;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.MINUTE);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 60 * 1000 - 1;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.MINUTE);
        });

        it('returns hours for large time scale', () => {
            var startTime = Date.now();
            var root = {
                startTime: startTime,
                endTime: startTime + 10 * 60 * 60 * 1000
            };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.HOUR);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 1000 * 60;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.HOUR);
        });

        it('returns relative time scale for missing start or end time', () => {
            var root = {
                startTime: Date.now()
            };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.RELATIVE);
            root.endTime = Date.now();
            delete root.startTime;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.RELATIVE);
            delete root.endTime;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.RELATIVE);
        });
    });

    describe('resolveType', () => {
        it('returns null when there are no options', () => {
            var options = null,
                types = [Generator.randomCategory().id];
            expect(Utils.resolveType(types, options)).toBeNull();
            options = [];
            expect(Utils.resolveType(types, options)).toBeNull();
        });

        it('returns null when there are no types', () => {
            var options = Generator.getCategories(),
                types = null;
            expect(Utils.resolveType(types, options)).toBeNull();
            types = [];
            expect(Utils.resolveType(types, options)).toBeNull();
        });

        it('returns first option whose id is in types', () => {
            var options = Generator.getCategories(),
                option = Generator.randomCategory(),
                types = [option.id];
            expect(Utils.resolveType(types, options)).toEqual(option);
        });

        it('returns null if none of the options matches types', () => {
            var options = Generator.getCategories(),
                option = Generator.randomCategory(),
                types = [Generator.getRandomUri()];
            expect(Utils.resolveType(types, options)).toBeNull();
        })
    });
});
