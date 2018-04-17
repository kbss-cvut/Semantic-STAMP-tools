import React from "react";
import TestUtils from "react-addons-test-utils";
import Constants from "../../js/constants/Constants";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Location from "../../js/components/report/occurrence/Location";
import {messages} from "../../js/i18n/en.js";

describe("Location", () => {

    let onChange,
        occurrence;

    beforeEach(() => {
        onChange = jasmine.createSpy("onChange");
        occurrence = Generator.generateOccurrenceReport().occurrence;
    });

    it('renders named location by default', () => {
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        const inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            namedRadio = inputs.find(i => i.props.value === 'named'),
            gpsRadio = inputs.find(i => i.props.value === 'gps');
        expect(TestUtils.findRenderedDOMComponentWithTag(namedRadio, 'input').checked).toBeTruthy();
        expect(TestUtils.findRenderedDOMComponentWithTag(gpsRadio, 'input').checked).toBeFalsy();
        expect(inputs.find(i => {
            return i.props.type === 'text' && i.props.label === messages['occurrence.location.named.name']
        })).toBeDefined();
    });

    it('renders GPS location when selected', () => {
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        let inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            gpsRadio = inputs.find(i => i.props.value === 'gps');
        TestUtils.Simulate.change(TestUtils.findRenderedDOMComponentWithTag(gpsRadio, 'input'), {target: {checked: true}});
        inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default);
        let namedRadio = inputs.find(i => i.props.value === 'named');
        gpsRadio = inputs.find(i => i.props.value === 'gps');
        expect(TestUtils.findRenderedDOMComponentWithTag(namedRadio, 'input').checked).toBeFalsy();
        expect(TestUtils.findRenderedDOMComponentWithTag(gpsRadio, 'input').checked).toBeTruthy();
        expect(inputs.find(i => {
            return i.props.type === 'number' && i.props.label === messages['occurrence.location.gps.lat']
        })).toBeDefined();
        expect(inputs.find(i => {
            return i.props.type === 'number' && i.props.label === messages['occurrence.location.gps.long']
        })).toBeDefined();
    });

    it('renders existing location name when present in occurrence', () => {
        occurrence.location = {
            name: 'Letiste Praha',
            javaClass: Constants.NAMED_LOCATION_JAVA_CLASS
        };
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        const inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            locName = inputs.find(i => {
                return i.props.type === 'text' && i.props.label === messages['occurrence.location.named.name']
            });
        expect(locName).toBeDefined();
        expect(locName.getInputDOMNode().value).toEqual(occurrence.location.name);
    });

    it('renders existing GPS location when present in occurrence', () => {
        occurrence.location = {
            latitude: 50.0755,
            longitude: 14.4378,
            javaClass: Constants.GPS_LOCATION_JAVA_CLASS
        };
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        const inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            lat = inputs.find(i => {
                return i.props.type === 'number' && i.props.label === messages['occurrence.location.gps.lat']
            }),
            long = inputs.find(i => {
                return i.props.type === 'number' && i.props.label === messages['occurrence.location.gps.long']
            });
        expect(lat).toBeDefined();
        expect(lat.getInputDOMNode().value).toEqual(occurrence.location.latitude.toString());
        expect(long).toBeDefined();
        expect(long.getInputDOMNode().value).toEqual(occurrence.location.longitude.toString());
    });

    it('updates occurrence when location name changes', () => {
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        const inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            locName = inputs.find(i => {
                return i.props.type === 'text' && i.props.label === messages['occurrence.location.named.name']
            });
        const update = 'Letiste Praha';
        TestUtils.Simulate.change(locName.getInputDOMNode(), {target: {value: update}});
        expect(onChange).toHaveBeenCalled();
        const updateArg = onChange.calls.argsFor(0)[0];
        expect(updateArg.occurrence.location.javaClass).toEqual(Constants.NAMED_LOCATION_JAVA_CLASS);
        expect(updateArg.occurrence.location.name).toBeDefined();
        expect(updateArg.occurrence.location.name).toEqual(update);
    });

    it('updates occurrence when GPS location changes', () => {
        occurrence.location = {
            latitude: 50.0755,
            longitude: 14.4378,
            javaClass: Constants.GPS_LOCATION_JAVA_CLASS
        };
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        const inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            lat = inputs.find(i => {
                return i.props.type === 'number' && i.props.label === messages['occurrence.location.gps.lat']
            });
        const newLat = 49.195060;
        TestUtils.Simulate.change(lat.getInputDOMNode(), {target: {value: newLat.toString()}});
        expect(onChange).toHaveBeenCalled();
        const updateArg = onChange.calls.argsFor(0)[0];
        expect(updateArg.occurrence.location.javaClass).toEqual(Constants.GPS_LOCATION_JAVA_CLASS);
        expect(updateArg.occurrence.location.latitude).toBeDefined();
        expect(updateArg.occurrence.location.latitude).toEqual(newLat);
    });

    it('correctly changes type from named to GPS on update', () => {
        const component = Environment.render(<Location occurrence={occurrence} onChange={onChange}/>);
        let inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default),
            gpsRadio = inputs.find(i => i.props.value === 'gps');
        TestUtils.Simulate.change(TestUtils.findRenderedDOMComponentWithTag(gpsRadio, 'input'), {target: {checked: true}});
        inputs = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/Input').default);
        const long = inputs.find(i => {
            return i.props.type === 'number' && i.props.label === messages['occurrence.location.gps.long']
        });
        const newLong = 16.606837;
        TestUtils.Simulate.change(long.getInputDOMNode(), {target: {value: newLong.toString()}});
        expect(onChange).toHaveBeenCalled();
        const updateArg = onChange.calls.argsFor(0)[0];
        expect(updateArg.occurrence.location.javaClass).toEqual(Constants.GPS_LOCATION_JAVA_CLASS);
        expect(updateArg.occurrence.location.longitude).toBeDefined();
        expect(updateArg.occurrence.location.longitude).toEqual(newLong);
        expect(updateArg.occurrence.location.name).not.toBeDefined();
    });
});
