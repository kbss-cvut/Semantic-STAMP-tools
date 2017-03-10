'use strict';

describe('ReportController', function () {

    const React = require('react'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        RouterStore = require('../../js/stores/RouterStore'),
        ReportController = require('../../js/components/report/ReportController'),
        Routes = require('../../js/utils/Routes'),
        Constants = require('../../js/constants/Constants'),
        Vocabulary = require('../../js/constants/Vocabulary');

    beforeEach(function () {
        spyOn(Actions, 'loadOptions');
        Environment.mockCurrentUser();
        Environment.mockGantt();
    });

    it('Loads existing report when report key is passed in path params', function () {
        spyOn(Actions, 'loadReport');
        const params = {reportKey: 12345},
            controller = Environment.render(<ReportController params={params}/>),
            state = controller.state;
        expect(Actions.loadReport).toHaveBeenCalledWith(params.reportKey);
        expect(state.loading).toBeTruthy();
        expect(state.report).toBeNull();
    });

    it('Initializes new report when no key is specified', function () {
        const controller = Environment.render(<ReportController params={{}}/>),
            report = controller.state.report;

        expect(controller.state.loading).toBeFalsy();
        expect(report).toBeDefined();
        expect(report.isNew).toBeTruthy();
        expect(report.occurrence).toBeDefined();
        expect(report.occurrence.startTime).toBeDefined();
        expect(report.occurrence.endTime).toBeDefined();
    });

    it('initializes new report when no key is passed in updated props', () => {
        const TestParent = React.createClass({
            getInitialState: function () {
                return {
                    params: {
                        reportKey: 12345
                    }
                }
            },
            render() {
                return <ReportController ref={c => this.sut = c} {...this.state}/>;
            }
        });
        spyOn(Actions, 'loadReport');
        const params = {reportKey: 12345},
            parent = Environment.render(<TestParent/>),
            baseReport = Generator.generateOccurrenceReport();
        spyOn(RouterStore, 'getTransitionPayload').and.returnValue({
            reportType: Vocabulary.SAFETY_ISSUE_REPORT,
            basedOn: {
                report: baseReport,
                event: baseReport.occurrence
            }
        });
        parent.setState({
            params: {}
        });
        const controllerState = parent.sut.state;
        expect(controllerState.report.isNew).toBeTruthy();
        expect(controllerState.report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
        expect(controllerState.report.safetyIssue).toBeDefined();
        expect(controllerState.report.safetyIssue.basedOn).toBeDefined();
    });
});
