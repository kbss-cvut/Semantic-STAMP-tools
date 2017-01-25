'use strict';

import React from "react";
import {Button} from "react-bootstrap";
import DateTimePicker from "kbss-react-bootstrap-datetimepicker";
import JsonLdUtils from "jsonld-utils";
import Constants from "../../../constants/Constants";
import EventTypeTypeahead from "../../typeahead/EventTypeTypeahead";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import ObjectTypeResolver from "../../../utils/ObjectTypeResolver";
import OptionsStore from "../../../stores/OptionsStore";
import Vocabulary from "../../../constants/Vocabulary";

class FactorEditRow extends React.Component {
    static propTypes = {
        factor: React.PropTypes.object.isRequired,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    _onEventTypeSelected = (et) => {

    };

    _onDateChange = (date) => {

    };

    render() {
        const factor = this.props.factor,
            eventType = ObjectTypeResolver.resolveType(factor.eventType, OptionsStore.getOptions(Constants.OPTIONS.EVENT_TYPE)),
            eventTypeLabel = eventType ? JsonLdUtils.getJsonAttValue(eventType, Vocabulary.RDFS_LABEL) : factor.eventType;
        return <tr>
            <td className='report-row content-center inline'>
                <EventTypeTypeahead value={eventTypeLabel} focus={true} onSelect={this._onEventTypeSelected}/>
            </td>
            <td className='report-row content-center inline'>
                <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={factor.startTime.toString()}
                                onChange={this._onDateChange} size='small'
                                inputProps={{
                                    title: this.i18n('occurrence.start-time-tooltip'),
                                    className: 'inline-input',
                                    size: 12
                                }}/>
            </td>
            <td className='report-row content-center inline'>
                <DateTimePicker inputFormat='DD-MM-YY HH:mm:ss' dateTime={factor.endTime.toString()}
                                onChange={this._onDateChange} size='small'
                                inputProps={{
                                    title: this.i18n('occurrence.end-time-tooltip'),
                                    className: 'inline-input',
                                    size: 12
                                }}/>
            </td>
            <td className="report-row actions">
                <Button bsStyle='success' bsSize='small'>{this.i18n('save')}</Button>
                <Button bsSize='small' onClick={this.props.handlers.onEditCancel}>{this.i18n('cancel')}</Button>
            </td>
        </tr>;
    }
}

export default injectIntl(I18nWrapper(FactorEditRow));
