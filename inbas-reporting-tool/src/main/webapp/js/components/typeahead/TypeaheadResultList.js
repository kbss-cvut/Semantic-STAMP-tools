'use strict';

var React = require('react');

var TypeaheadResultList = React.createClass({

    render: function () {
        var listCls = this.props.options.length < 21 ? 'autocomplete-results' : 'autocomplete-results extended';
        if (this.props.customClasses.results) {
            listCls += ' ' + this.props.customClasses.results;
        }
        var items = [],
            option;
        for (var i = 0, len = this.props.options.length; i < len; i++) {
            option = this.props.options[i];
            var onClick = this.onClick.bind(this, option);
            items.push(<li className='btn-link item' key={'typeahead-result-' + i} title={option.description}
                           onClick={onClick}>{this.getOptionLabel(option)}</li>);
        }
        return <ul className={listCls}>
            {items}
        </ul>;
    },

    getOptionLabel: function (option) {
        if (typeof this.props.displayOption === 'function') {
            return this.props.displayOption(option);
        } else {
            return option[this.props.displayOption];
        }
    },

    onClick: function (option, event) {
        event.preventDefault();
        this.props.onOptionSelected(option, event);
    }
});

module.exports = TypeaheadResultList;
