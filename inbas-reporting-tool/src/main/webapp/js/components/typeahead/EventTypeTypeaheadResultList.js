import React from "react";
import PropTypes from "prop-types";
import {Label} from "react-bootstrap";
import classNames from "classnames";
import FactorStyleInfo from "../../utils/FactorStyleInfo";

const ResultListItem = (props) => {
    const option = props.option,
        label = typeof props.displayOption === 'function' ? props.displayOption(option) : option[props.displayOption],
        styleInfo = FactorStyleInfo.getStyleInfo(option.type),
        className = classNames('btn-link', 'item', {'hover': props.hover});

    return <li className={className} title={option.description} onClick={props.onClick}>
        {styleInfo.value ?
            <Label bsStyle={styleInfo.bsStyle} title={styleInfo.title}
                   className='autocomplete-results-item'>{styleInfo.value}</Label> : null}
        {label}
    </li>;
};

ResultListItem.propTypes = {
    option: PropTypes.object.isRequired,
    displayOption: PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.func]).isRequired,
    onClick: PropTypes.func.isRequired,
    hover: PropTypes.bool
};

class EventTypeTypeaheadResultList extends React.Component {

    constructor(props) {
        super(props);
    }

    _onClick(option, evt) {
        evt.preventDefault();
        this.props.onOptionSelected(option);
    }

    render() {
        const options = this.props.options;
        let listCls = options.length < 21 ? 'autocomplete-results event-type' : 'autocomplete-results extended event-type';
        if (this.props.customClasses.results) {
            listCls += ' ' + this.props.customClasses.results;
        }
        const items = [];
        for (let i = 0, len = options.length; i < len; i++) {
            const option = options[i],
                onClick = this._onClick.bind(this, option);
            items.push(<ResultListItem option={option} displayOption={this.props.displayOption} onClick={onClick}
                                       hover={i === this.props.selectionIndex} key={'option-' + i}/>);
        }
        return <ul className={listCls}>
            {items}
        </ul>;
    }
}

EventTypeTypeaheadResultList.propTypes = {
    options: PropTypes.array.isRequired,
    customClasses: PropTypes.object.isRequired,
    onOptionSelected: PropTypes.func.isRequired,
    displayOption: PropTypes.oneOfType([React.PropTypes.string, React.PropTypes.func]).isRequired,
    selectionIndex: PropTypes.number
};

export default EventTypeTypeaheadResultList;
