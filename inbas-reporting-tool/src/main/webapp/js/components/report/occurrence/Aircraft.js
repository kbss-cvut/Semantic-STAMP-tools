'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import assign from "object-assign";
import JsonLdUtils from "jsonld-utils";
import Typeahead from "react-bootstrap-typeahead";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";

class Aircraft extends React.Component {
    static propTypes = {
        aircraft: React.PropTypes.object,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            aircraftPresent: this.props.aircraft !== undefined && this.props.aircraft !== null
        };
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
        Actions.loadOptions(Constants.OPTIONS.AIRCRAFT_OPERATOR);
        Actions.loadOptions(Constants.OPTIONS.AIRCRAFT_TYPE);
    }

    _onOptionsLoaded = (type) => {
        if (type === Constants.OPTIONS.AIRCRAFT_OPERATOR || type === Constants.OPTIONS.AIRCRAFT_TYPE) {
            this.forceUpdate();
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _toggleAircraft = () => {
        this.setState({aircraftPresent: !this.state.aircraftPresent});
    };

    _onAircraftTypeSelected = (opt) => {
        const aircraft = assign({}, this.props.aircraft);
        aircraft.types = [opt.id];
        this.props.onChange({aircraft: aircraft});
    };

    _onOperatorSelected = (opt) => {
        const aircraft = assign({}, this.props.aircraft);
        aircraft.operator = {
            uri: opt.id,
            name: opt.name
        };
        this.props.onChange({aircraft: aircraft});
    };

    render() {
        const aircraftType = JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions(Constants.OPTIONS.AIRCRAFT_TYPE)),
            operator = JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions(Constants.OPTIONS.AIRCRAFT_OPERATOR));
        return <Panel bsStyle='info' header={this._renderHeader()} collapsible expanded={this.state.aircraftPresent}>
            <div className='row'>
                <div className='col-xs-4'>
                    <Typeahead label={this.i18n('occurrence.aircraft.type')}
                               formInputOption='id' optionsButton={true}
                               placeholder={this.i18n('occurrence.aircraft.type')}
                               onOptionSelected={this._onAircraftTypeSelected} filterOption='name'
                               value={null} size='small'
                               displayOption='name' options={aircraftType}
                               customListComponent={TypeaheadResultList}/>
                </div>
                <div className='col-xs-4'>
                    <Typeahead label={this.i18n('occurrence.aircraft.operator')}
                               formInputOption='id'
                               placeholder={this.i18n('occurrence.aircraft.operator')}
                               onOptionSelected={this._onOperatorSelected} filterOption='name'
                               value={null} size='small'
                               displayOption='name' options={operator}
                               customListComponent={TypeaheadResultList}/>
                </div>
            </div>
        </Panel>;
    }

    _renderHeader() {
        return <Input type='checkbox' onChange={this._toggleAircraft} value='aircraftPresent'
                      checked={this.state.aircraftPresent}
                      label={<h5 className='panel-title'
                                 style={{padding: '2px 0 0 0'}}>{this.i18n('occurrence.aircraft')}</h5>}
                      title={this.i18n('occurrence.aircraft.presence-tooltip')} className='panel-toggle'/>;
    }
}

export default injectIntl(I18nWrapper(Aircraft));
