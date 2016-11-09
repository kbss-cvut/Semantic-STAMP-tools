'use strict';

import Generator from "../environment/Generator";
import ObjectTypeResolver from "../../js/utils/ObjectTypeResolver";

describe('ObjectTypeResolver', () => {

    it('returns option representing object\'s type', () => {
        var options = Generator.getJsonLdSample(),
            type = options[Generator.getRandomInt(options.length)],
            object = {
                types: [Generator.getRandomUri(), type['@id']]
            },

            result = ObjectTypeResolver.resolveType(object, options);
        expect(result).toEqual(type);
    });

    it('returns option representing type when type URI is specified directly instead of object', () => {
        var options = Generator.getJsonLdSample(),
            type = options[Generator.getRandomInt(options.length)],
            object = type['@id'],

            result = ObjectTypeResolver.resolveType(object, options);
        expect(result).toEqual(type);
    });

    it('returns object representing first matching type when array is passed in instead of object', () => {
        var options = Generator.getJsonLdSample(),
            type = options[Generator.getRandomInt(options.length)],
            object = [type['@id']],

            result = ObjectTypeResolver.resolveType(object, options);
        expect(result).toEqual(type);
    });
});
