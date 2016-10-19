'use strict';

import React from "react";
import classNames from "classnames";
import {Label} from "react-bootstrap";
import I18nStore from "../../stores/I18nStore";

const ResultListItem = (props) => {
    var option = props.option,
        label = typeof props.displayOption === 'function' ? props.displayOption(option) : option[props.displayOption];

    return <li className='btn-link item' title={option.description} onClick={props.onClick}>
        <Label className='item-label'>{I18nStore.i18n(option.getLabel())}</Label>
        {label}
    </li>;
};

ResultListItem.propTypes = {
    option: React.PropTypes.object.isRequired,
    displayOption: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.func]).isRequired,
    onClick: React.PropTypes.func.isRequired
};

class ReportSearchResultList extends React.Component {
    static propTypes = {
        options: React.PropTypes.array.isRequired,
        customClasses: React.PropTypes.object.isRequired,
        onOptionSelected: React.PropTypes.func.isRequired,
        displayOption: React.PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.func]).isRequired
    };

    constructor(props) {
        super(props);
    }

    _onClick(option, evt) {
        evt.preventDefault();
        this.props.onOptionSelected(option);
    }

    render() {
        var options = this.props.options,
            listCls = classNames({
                'autocomplete-results': options.length < 21,
                'autocomplete-results extended': options.length >= 21
            }, this.props.customClasses.results);
        var items = [];
        for (var i = 0, len = options.length; i < len; i++) {
            const option = options[i];
            var onClick = this._onClick.bind(this, option);
            items.push(<ResultListItem option={option} displayOption={this.props.displayOption} onClick={onClick}
                                       key={'option-' + i}/>);
        }
        return <ul className={listCls}>
            {items}
        </ul>;
    }
}

export default ReportSearchResultList;
