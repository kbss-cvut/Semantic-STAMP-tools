'use strict';

jest.dontMock('../../js/stores/RouterStore');

describe('RouterStore tests', function () {

    var RouterStore;

    beforeEach(function () {
        RouterStore = require('../../js/stores/RouterStore');
    });

    it('Resets route payload when none is received on transition triggered by action', function () {
        var route = 'report_new';
        var payload = {id: 12345};
        RouterStore.setTransitionPayload(route, payload);
        RouterStore.setTransitionPayload(route);
        expect(RouterStore.getTransitionPayload()).toBeUndefined();
    });
});
