'use strict';

import rewire from "rewire";
import {WizardGenerator} from "semforms";
import Ajax from "../../js/utils/Ajax";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";

var InbasWizardGenerator = rewire("../../js/components/wizard/generator/WizardGenerator");

const WIZARD_TITLE = 'Test wizard';

describe('WizardGenerator', () => {

    var report, event,
        renderCallback;

    beforeEach(() => {
        spyOn(Ajax, 'post').and.returnValue(Ajax);
        spyOn(Ajax, 'end');
        report = Generator.generateOccurrenceReport();
        event = {
            referenceId: 117,
            eventType: Generator.getRandomUri(),
            question: {
                uri: Generator.getRandomUri()
            }
        };
        renderCallback = jasmine.createSpy('renderCallback');
        var logger = Environment.mockLogger();
        InbasWizardGenerator.__set__('Logger', logger);
    });

    describe('generateWizard', () => {

        it('sends event reference id and encoded event type as URL parameters', () => {
            InbasWizardGenerator.generateWizard(report, event, WIZARD_TITLE, renderCallback);

            expect(Ajax.post).toHaveBeenCalled();
            var args = Ajax.post.calls.argsFor(0);
            expect(args[1]).toEqual(report);
            var url = args[0];
            expect(url.indexOf('event=' + event.referenceId)).not.toEqual(-1);
            expect(url.indexOf('eventType=' + encodeURIComponent(event.eventType))).not.toEqual(-1);
        });

        it('renders default wizard when REST error occurs', () => {
            spyOn(WizardGenerator, 'createDefaultWizard');
            Ajax.end.and.callFake((onSuccess, onError) => {
                onError();
            });
            InbasWizardGenerator.generateWizard(report, event, WIZARD_TITLE, renderCallback);

            expect(WizardGenerator.createDefaultWizard).toHaveBeenCalledWith(event.question, WIZARD_TITLE, renderCallback);
        });
    });

    describe('generateSummaryWizard', () => {
        it('sends only the report to wizard generator service', () => {
            InbasWizardGenerator.generateSummaryWizard(report, WIZARD_TITLE, renderCallback);

            expect(Ajax.post).toHaveBeenCalled();
            var args = Ajax.post.calls.argsFor(0);
            expect(args[1]).toEqual(report);
            var url = args[0];
            expect(url.indexOf('=')).toEqual(-1);   // No params expected
        });

        it('renders default wizard when REST error occurs', () => {
            spyOn(WizardGenerator, 'createDefaultWizard');
            Ajax.end.and.callFake((onSuccess, onError) => {
                onError();
            });
            InbasWizardGenerator.generateSummaryWizard(report, WIZARD_TITLE, renderCallback);

            expect(WizardGenerator.createDefaultWizard).toHaveBeenCalledWith({}, WIZARD_TITLE, renderCallback);
        });
    });
});
