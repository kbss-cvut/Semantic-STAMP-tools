'use strict';

describe('Unprocessed reports', () => {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        Environment = require('../environment/Environment'),
        Well = require('react-bootstrap').Well,
        Generator = require('../environment/Generator').default,
        Actions = require('../../js/actions/Actions'),
        Routes = require('../../js/utils/Routes'),
        Routing = require('../../js/utils/Routing'),
        UnprocessedReports = require('../../js/components/dashboard/UnprocessedReports').default;

    it('shows no message when no unprocessed reports are found', () => {
        var component = Environment.render(<UnprocessedReports />),
            reports = generateReports(true).reports;
        component._onReportsLoaded({
            action: Actions.loadAllReports,
            reports: reports
        });
        var cnt = TestUtils.scryRenderedComponentsWithType(component, Well);
        expect(cnt.length).toEqual(0);
    });

    function generateReports(noUnprocessed) {
        var reports = [],
            unprocessedCount = 0,
            report;
        for (var i = 0, len = Generator.getRandomPositiveInt(10, 20); i < len; i++) {
            report = Generator.generateOccurrenceReport();
            report.phase = noUnprocessed || !Generator.getRandomBoolean() ? Generator.getRandomUri() : UnprocessedReports.UNPROCESSED_PHASE;
            if (report.phase === UnprocessedReports.UNPROCESSED_PHASE) {
                unprocessedCount++;
            }
            reports.push(report);
        }
        return {
            reports: reports,
            unprocessedCount: unprocessedCount
        }
    }

    it('correctly counts number of unprocessed reports', () => {
        var component = Environment.render(<UnprocessedReports />),
            generated = generateReports();
        component._onReportsLoaded({
            action: Actions.loadAllReports,
            reports: generated.reports
        });
        expect(component.state.count).toEqual(generated.unprocessedCount);
    });

    it('shows message stating number of unprocessed reports when there are unprocessed', () => {
        var component = Environment.render(<UnprocessedReports />),
            generated = generateReports();
        component._onReportsLoaded({
            action: Actions.loadAllReports,
            reports: generated.reports
        });
        var well = TestUtils.findRenderedComponentWithType(component, Well);
        expect(well).not.toBeNull();
    });

    it('sets filter as transition payload when transitioning to reports', () => {
        var component = Environment.render(<UnprocessedReports />),
            generated = generateReports();
        component._onReportsLoaded({
            action: Actions.loadAllReports,
            reports: generated.reports
        });
        spyOn(Routing, 'transitionTo');
        UnprocessedReports._goToReports();
        expect(Routing.transitionTo).toHaveBeenCalledWith(Routes.reports, {
                payload: {
                    filter: {
                        phase: UnprocessedReports.UNPROCESSED_PHASE
                    }
                }
            }
        );
    });
});
