'use strict';

jest.dontMock('../../js/stores/RouterStore');

describe('RouterStore tests', function () {

    var RouterStore = require('../../js/stores/RouterStore');

    it('Resets route payload when none is received on transition payload set', function () {
        var route = 'report_new';
        var payload = {id: 12345};
        RouterStore.setTransitionPayload(route, payload);
        expect(RouterStore.getTransitionPayload(route)).toEqual(payload);
        RouterStore.setTransitionPayload(route);
        expect(RouterStore.getTransitionPayload()).toBeUndefined();
    });
});
