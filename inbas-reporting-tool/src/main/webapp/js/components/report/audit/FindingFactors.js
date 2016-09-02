'use strict';

import React from "react";
import JsonLdUtils from "jsonld-utils";
import {Glyphicon, Label} from "react-bootstrap";
import Typeahead from "react-bootstrap-typeahead";
import Actions from "../../../actions/Actions";
import FactorStyleInfo from "../../../utils/FactorStyleInfo";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import TypeaheadResultList from "../../typeahead/EventTypeTypeaheadResultList";
import TypeaheadStore from "../../../stores/TypeaheadStore";
import Utils from "../../../utils/Utils";

const FACTOR_ROW_LENGTH = 3;

// TODO Extract a generic component from this. It will be reused in other parts of the application
class FindingFactors extends React.Component {
    static propTypes = {
        factors: React.PropTypes.array,
        onChange: React.PropTypes.func
    };

    static defaultProps = {
        factors: []
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            eventType: JsonLdUtils.processTypeaheadOptions(TypeaheadStore.getEventTypes())
        }
    }

    componentDidMount() {
        if (this.state.eventType.length === 0) {
            Actions.loadEventTypes();
        }
        this.unsubscribe = TypeaheadStore.listen(this._onEventTypesLoaded);
    }

    _onEventTypesLoaded = (data) => {
        if (data.action === Actions.loadEventTypes) {
            this.setState({eventType: JsonLdUtils.processTypeaheadOptions(data.data)});
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onFactorTypeSelected = (option) => {
        var factors = this.props.factors.slice();
        factors.push(option.id);
        this.props.onChange({factors: factors});
        this.typeahead.setEntryText('');
    };

    _onRemoveFactor = (factor) => {
        var factors = this.props.factors.slice();
        factors.splice(factors.indexOf(factor), 1);
        this.props.onChange({factors: factors});
    };

    render() {
        return <div className='col-xs-12 form-group'>
            <div className='row'>
                <div className='col-xs-4'>
                    <label className="control-label">{this.i18n('audit.finding.factors')}</label>
                </div>
            </div>
            <div className='row'>
                <div className='col-xs-4'>
                    <Typeahead ref={(c) => this.typeahead = c} className='form-group form-group-sm'
                               formInputOption='id' placeholder={this.i18n('audit.finding.factors.placeholder')}
                               onOptionSelected={this._onFactorTypeSelected}
                               filterOption='name' value={null} displayOption='name' options={this.state.eventType}
                               customListComponent={TypeaheadResultList}/>
                </div>
            </div>
            <div className='row'>
                <div className='col-xs-12'>
                    {this._renderFactors()}
                </div>
            </div>
        </div>;
    }

    _renderFactors() {
        var factors = this.props.factors,
            rows = [],
            items = [];
        for (var i = 0, len = factors.length; i < len; i++) {
            items.push(<Factor key={'factor_' + i} factor={factors[i]} eventTypes={this.state.eventType}
                               onRemove={this._onRemoveFactor}/>);
            if (i === FACTOR_ROW_LENGTH) {
                FindingFactors._addRow(rows, items);
                items = [];
            }
        }
        if (items.length > 0) {
            FindingFactors._addRow(rows, items);
        }
        return rows;
    }

    static _addRow(rows, items) {
        rows.push(<div className={'row' + (rows.length > 0 ? ' finding-factor-row-with-margin' : '')}
                       key={'row-' + rows.length}>
            <div className='col-xs-12'>{items}</div>
        </div>);
    }
}

var Factor = (props) => {
    var factor = props.factor,
        type = Utils.resolveType([factor], props.eventTypes),
        styleInfo = FactorStyleInfo.getStyleInfo(type ? type.type : '');

    return <Label bsStyle={styleInfo.bsStyle} className='finding-factor-label' title={styleInfo.title}>
        {type ? type.name : factor}
        <Glyphicon className='remove-icon-glyph' glyph='remove' onClick={() => props.onRemove(factor)}
                   title={props.i18n('audit.finding.factors.remove-tooltip')}/>
    </Label>;
};

Factor.propTypes = {
    factor: React.PropTypes.string.isRequired,
    eventTypes: React.PropTypes.array,
    onRemove: React.PropTypes.func.isRequired
};

Factor = injectIntl(I18nWrapper(Factor));

export default injectIntl(I18nWrapper(FindingFactors));
