'use strict';

var React = require('react');
var Input = require('../Input');
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;

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
            'phase': 'all',
            'occurrenceCategory.id': 'all'
        }
    },

    onSelect: function (e) {
        var value = e.target.value;
        var change = {};
        change[e.target.name] = value;
        this.setState(change);
        this.props.onFilterChange(change);
    },


    render: function () {
        return (
            <Row className='show-grid filter'>
                <Col xs={2}><h4>{this.i18n('reports.filter.label')}</h4></Col>
                <Col xs={1}>&nbsp;</Col>
                <Col xs={4}>&nbsp;</Col>
                <Col xs={2} style={{margin: '0 0 -15px 0', padding: '3px 3px 0 3px'}}>
                    <Input type='select' name='occurrenceCategory.id' value={this.state.category}
                           title={this.i18n('reports.table-classification.tooltip')}
                           onChange={this.onSelect}>
                        {this.renderClassificationOptions()}
                    </Input>
                </Col>
                <Col xs={1} style={{margin: '0 0 -15px 0', padding: '3px 3px 0 3px'}}>
                    <Input type='select' name='phase' value={this.state.type}
                           title={this.i18n('reports.filter.type.tooltip')}
                           onChange={this.onSelect}>
                        <option key='type-all' value='all'>{this.i18n('reports.filter.type.all')}</option>
                        <option key='type-preliminary'
                                value={Constants.PRELIMINARY_REPORT_PHASE}>{this.i18n('reports.filter.type.preliminary')}</option>
                        <option key='type-investigation'
                                value={Constants.INVESTIGATION_REPORT_PHASE}>{this.i18n('investigation.type')}</option>
                    </Input>
                </Col>
                <Col xs={2}>&nbsp;</Col>
            </Row>);
    },

    renderClassificationOptions: function () {
        var categories = [],
            options = [],
            category,
            reports = this.props.reports;
        options.push(<option key='category-all' value='all'>{this.i18n('reports.filter.type.all')}</option>);
        for (var i = 0, len = reports.length; i < len; i++) {
            category = reports[i].occurrenceCategory;
            if (categories.indexOf(category.id) !== -1) {
                continue;
            }
            categories.push(category.id);
            options.push(<option key={'category-' + category.id} value={category.id}>{category.name}</option>);
        }
        return options;
    }
});

module.exports = injectIntl(ReportsFilter);
