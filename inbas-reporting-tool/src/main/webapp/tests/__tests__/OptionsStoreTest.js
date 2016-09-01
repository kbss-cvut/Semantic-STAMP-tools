'use strict';

import Ajax from "../../js/utils/Ajax";
import Generator from "../environment/Generator";
import OptionsStore from "../../js/stores/OptionsStore";

describe('OptionsStore', () => {

    beforeEach(() => {
        spyOn(Ajax, 'get').and.returnValue(Ajax);
        spyOn(Ajax, 'end');
    });

    it('adds parameter values and query parameters to URL when specified', () => {
        var type = 'test',
            params = {
                'pOne': 'vOne',
                'pTwo': encodeURIComponent(Generator.getRandomUri())
            };
        OptionsStore.onLoadOptions(type, params);
        var url = Ajax.get.calls.argsFor(0)[0];
        expect(url.indexOf('type=' + type)).not.toEqual(-1);
        Object.getOwnPropertyNames(params).forEach((key) => {
            expect(url.indexOf(key + '=' + params[key])).not.toEqual(-1);
        });
    });

    it('adds only option type as query param when no parameters are specified', () => {
        var type = 'test';
        OptionsStore.onLoadOptions(type);
        var url = Ajax.get.calls.argsFor(0)[0];
        expect(url.indexOf('type=' + type)).not.toEqual(-1);
        var cnt = 0;
        for (var i = 0, len = url.length; i < len; i++) {
            if (url.charAt(i) === '=') {
                cnt++;
            }
        }
        expect(cnt).toEqual(1);
    });
});
