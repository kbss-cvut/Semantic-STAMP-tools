import React from "react";
import PropTypes from "prop-types";
import classNames from "classnames";

class TypeaheadResultList extends React.Component {

    render() {
        let listCls = this.props.options.length < 21 ? 'autocomplete-results' : 'autocomplete-results extended';
        if (this.props.customClasses.results) {
            listCls += ' ' + this.props.customClasses.results;
        }
        const items = [],
            classes = ['btn-link', 'item'];
        for (let i = 0, len = this.props.options.length; i < len; i++) {
            const option = this.props.options[i],
                onClick = this.onClick.bind(this, option),
                className = classNames(classes, {'hover': i === this.props.selectionIndex});
            items.push(<li className={className} key={'typeahead-result-' + i}
                           title={TypeaheadResultList.getOptionTitle(option)}
                           onClick={onClick}>{this.getOptionLabel(option)}</li>);
        }
        return <ul className={listCls}>
            {items}
        </ul>;
    }

    static getOptionTitle(option) {
        return option.description ? option.description : undefined;
    }

    getOptionLabel(option) {
        if (typeof this.props.displayOption === 'function') {
            return this.props.displayOption(option);
        } else {
            return option[this.props.displayOption];
        }
    }

    onClick(option, event) {
        event.preventDefault();
        this.props.onOptionSelected(option);
    }
}

TypeaheadResultList.propTypes = {
    options: PropTypes.array,
    customClasses: PropTypes.object,
    customValue: PropTypes.string,
    selectionIndex: PropTypes.number,
    onOptionSelected: PropTypes.func,
    displayOption: PropTypes.oneOfType([PropTypes.func, PropTypes.string]),
    defaultClassNames: PropTypes.bool
};

TypeaheadResultList.defaultProps = {
    selectionIndex: null,
    customClasses: {},
    customValue: null,
    onOptionSelected: function (option) {
    },
    defaultClassNames: true
};

export default TypeaheadResultList;
