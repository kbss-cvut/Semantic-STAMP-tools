/**
 * @jsx
 */

'use strict';

var React = require('react');
var FactorStyleInfo = require('../../utils/FactorStyleInfo');

var EventTypeResultList = React.createClass({

    render: function () {
        var listCls = this.props.options.length < 21 ? 'autocomplete-results event-type' : 'autocomplete-results extended event-type';
        if (this.props.customClasses.results) {
            listCls += ' ' + this.props.customClasses.results;
        }
        var items = [];
        for (var i = 0, len = this.props.options.length; i < len; i++) {
            var option = this.props.options[i];
            var onClick = this.onClick.bind(this, option);
            items.push(<li className='btn-link item' key={'typeahead-result-' + i} title={option.description}
                           onClick={onClick}>
                {this.renderIcon(option)}
                {this.getOptionLabel(option)}
            </li>);
        }
        return (
            <ul className={listCls}>
                {items}
            </ul>
        );
    },

    renderIcon: function (option) {
        var styleInfo = FactorStyleInfo.getStyleInfo(option.type);

        return (<img src={styleInfo.icon} className={styleInfo.cls} title={styleInfo.title}/>);
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
        this.props.onOptionSelected(option);
    }
});

module.exports = EventTypeResultList;