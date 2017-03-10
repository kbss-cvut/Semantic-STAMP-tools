'use strict';

import React from "react";
import Actions from "../../js/actions/Actions";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import ReportStore from "../../js/stores/ReportStore";
import Routes from "../../js/utils/Routes";
import Routing from "../../js/utils/Routing";
import EccairsReportButton from "../../js/components/report/occurrence/EccairsReportButton";

describe('ECCAIRS report button', () => {

    let report;

    beforeEach(() => {
        report = Generator.generateOccurrenceReport();
        report.isEccairsReport = () => {
            return true;
        };
    });

    it('transitions to the loaded ECCAIRS report when it exists', () => {
        spyOn(Routing, 'transitionTo');
        const eccairsKey = "117";
        Environment.render(<EccairsReportButton report={report}/>);
        ReportStore.trigger({
            action: Actions.loadEccairsReport,
            key: eccairsKey
        });
        expect(Routing.transitionTo).toHaveBeenCalled();
        const args = Routing.transitionTo.calls.argsFor(0);
        expect(args[0]).toEqual(Routes.editReport);
        expect(args[1].params.reportKey).toEqual(eccairsKey);
    });

    it('displays error message when ECCAIRS report cannot be loaded', () => {
        spyOn(Routing, 'transitionTo');
        spyOn(Actions, 'publishMessage');
        Environment.render(<EccairsReportButton report={report}/>);
        ReportStore.trigger({
            action: Actions.loadEccairsReport,
            key: null
        });
        expect(Routing.transitionTo).not.toHaveBeenCalled();
        expect(Actions.publishMessage).toHaveBeenCalled();
    });
});
