/**
 * @jsx
 */
'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('../Input');

var Constants = require('../../constants/Constants');
var injectIntl = require('../../utils/injectIntl');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportsFilter = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        onFilterChange: React.PropTypes.func.isRequired,
        reports: React.PropTypes.array
    },

    getInitialState: function () {
        return {
            'phase': Constants.FILTER_DEFAULT,
            'occurrence.eventType': Constants.FILTER_DEFAULT
        }
    },

    onSelect: function (e) {
        var value = e.target.value;
        var change = {};
        change[e.target.name] = value;
        this.setState(change);
        this.props.onFilterChange(change);
    },

    onResetFilters: function () {
        var newState = {};
        for (var key in this.state) {
            newState[key] = Constants.FILTER_DEFAULT;
        }
        this.setState(newState);
        this.props.onFilterChange(newState);
    },


    render: function () {
        return (
            <tr className='filter'>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td >
                    <div style={{margin: '0 0 -15px 0'}}>
                        <Input type='select' name='occurrence.eventType' value={this.state['occurrence.eventType']}
                               title={this.i18n('reports.table-classification.tooltip')}
                               onChange={this.onSelect}>
                            {this.renderClassificationOptions()}
                        </Input>
                    </div>
                </td>
                <td >
                    <div style={{margin: '0 0 -15px 0'}}>
                        <Input type='select' name='phase' value={this.state['phase']}
                               title={this.i18n('reports.filter.type.tooltip')}
                               onChange={this.onSelect}>
                            <option key='type-all'
                                    value={Constants.FILTER_DEFAULT}>{this.i18n('reports.filter.type.all')}</option>
                            <option key='type-preliminary'
                                    value={Constants.PRELIMINARY_REPORT_PHASE}>{this.i18n('reports.filter.type.preliminary')}</option>
                            <option key='type-investigation'
                                    value={Constants.INVESTIGATION_REPORT_PHASE}>{this.i18n('investigation.type')}</option>
                        </Input>
                    </div>
                </td>
                <td >
                    <Button bsStyle='primary' bsSize='small' className='reset-filters'
                            onClick={this.onResetFilters}>{this.i18n('reports.filter.reset')}</Button>
                </td>
            </tr>
        );
    },

    renderClassificationOptions: function () {
        var categories = [],
            options = [],
            category,
            reports = this.props.reports;
        options.push(<option key='category-all'
                             value={Constants.FILTER_DEFAULT}>{this.i18n('reports.filter.type.all')}</option>);
        for (var i = 0, len = reports.length; i < len; i++) {
            category = reports[i].occurrenceCategory;
            if (!category || categories.indexOf(category.id) !== -1) {
                continue;
            }
            categories.push(category.id);
            options.push(<option key={'category-' + category.id} value={category.id}>{category.name}</option>);
        }
        return options;
    }
});

module.exports = injectIntl(ReportsFilter);
