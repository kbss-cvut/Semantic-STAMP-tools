'use strict';

describe('Ajax utility', function () {

    var rewire = require('rewire'),
        Ajax = rewire('../../js/utils/Ajax'),
        Routes = require('../../js/utils/Routes'),
        reqMock, RoutingMock, LoggerMock, UtilsMock,
        reqMockMethods = ['get', 'put', 'post', 'del', 'send', 'accept', 'set', 'end'];

    beforeEach(function () {
        // We need only the 'end' function
        reqMock = jasmine.createSpyObj('request', reqMockMethods);
        initRequestMock();
        RoutingMock = jasmine.createSpyObj('Routing', ['transitionTo', 'transitionToHome', 'saveOriginalTarget']);
        // Just prevent log messages in test output
        LoggerMock = jasmine.createSpyObj('Logger', ['warn', 'log', 'error']);
        UtilsMock = jasmine.createSpyObj('Utils', ['getPathFromLocation']);
        Ajax.__set__('request', reqMock);
        Ajax.__set__('Routing', RoutingMock);
        Ajax.__set__('Logger', LoggerMock);
        Ajax.__set__('Utils', UtilsMock);
    });

    function initRequestMock() {
        for (var i = 0; i < reqMockMethods.length; i++) {
            // All mock methods just return the instance to adhere to the builder pattern implemented by request
            reqMock[reqMockMethods[i]].and.callFake(function () {
                return reqMock;
            });
        }
    }

    it('transitions to login screen when 401 status is returned', function () {
        reqMock.end.and.callFake(function (fn) {
            var err = {
                status: 401
            };
            fn(err, {});
        });
        UtilsMock.getPathFromLocation.and.returnValue('reports');
        Ajax.get('rest/reports').end();

        expect(reqMock.end).toHaveBeenCalled();
        expect(RoutingMock.transitionTo).toHaveBeenCalledWith(Routes.login);
    });

    it('saves original target route before transitioning to login when 401 status is returned', function () {
        var path = Routes.reports.path;
        reqMock.end.and.callFake(function (fn) {
            var err = {
                status: 401
            };
            fn(err, {});
        });
        UtilsMock.getPathFromLocation.and.returnValue(path);

        Ajax.get('rest/reports').end();

        expect(RoutingMock.saveOriginalTarget).toHaveBeenCalledWith({path: path});
        expect(RoutingMock.transitionTo).toHaveBeenCalledWith(Routes.login);
    });

    it('does not transition anywhere when the user is on register or login screen', function() {
        var path = Routes.login.path;
        reqMock.end.and.callFake(function (fn) {
            var err = {
                status: 401
            };
            fn(err, {});
        });
        UtilsMock.getPathFromLocation.and.returnValue(path);

        Ajax.get('rest/users/current').end();
        expect(RoutingMock.saveOriginalTarget).not.toHaveBeenCalled();
        expect(RoutingMock.transitionTo).not.toHaveBeenCalled();

        path = Routes.register.path;
        UtilsMock.getPathFromLocation.and.returnValue(path);

        Ajax.get('rest/users/current').end();
        expect(RoutingMock.saveOriginalTarget).not.toHaveBeenCalled();
        expect(RoutingMock.transitionTo).not.toHaveBeenCalled();
    });

    it('calls success handler when it is defined and success response is returned', function () {
        var resp = {
                status: 200,
                body: {
                    uri: 'http://someUri',
                    key: '12345'
                }
            },
            successHandler = jasmine.createSpy('successHandler');
        reqMock.end.and.callFake(function (fn) {
            fn(null, resp);
        });

        Ajax.get('rest/reports').end(successHandler);

        expect(successHandler).toHaveBeenCalledWith(resp.body, resp);
    });

    it('does nothing when no success handler is passed and success response is returned', function () {
        var resp = {
            status: 204
        };
        reqMock.end.and.callFake(function (fn) {
            fn(null, resp);
        });
        spyOn(Ajax, '_handleError');

        Ajax.del('rest/reports').end();

        expect(Ajax._handleError).not.toHaveBeenCalled();
    });

    it('calls error handler when it is defined and error response is returned', function () {
        var err = {
                status: 404,
                response: {
                    text: JSON.stringify({message: 'Resource not found'})
                }
            }, resp = {
                status: 404
            },
            successHandler = jasmine.createSpy('successHandler'),
            errorHandler = jasmine.createSpy('errorHandler');
        reqMock.end.and.callFake(function (fn) {
            fn(err, resp);
        });
        spyOn(Ajax, '_handleError');

        Ajax.get('rest/reports/12345').end(null, errorHandler);

        expect(errorHandler).toHaveBeenCalledWith(JSON.parse(err.response.text));
        expect(successHandler).not.toHaveBeenCalled();
        expect(Ajax._handleError).not.toHaveBeenCalled();
    });

    it('logs error when no error handler is defined and error response is returned', function () {
        var err = {
            status: 404,
            response: {
                text: JSON.stringify({message: 'Resource not found', requestUri: 'rest/reports/12345'}),
                req: {
                    method: 'GET'
                }
            }
        }, resp = {
            status: 404
        };
        reqMock.end.and.callFake(function (fn) {
            fn(err, resp);
        });
        spyOn(Ajax, '_handleError').and.callThrough();

        Ajax.get('rest/reports/12345').end();

        expect(Ajax._handleError).toHaveBeenCalledWith(err);
    });
});
