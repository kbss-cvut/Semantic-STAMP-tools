'use strict';

var React = require('react');
var Input = require('../Input');
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;

var injectIntl = require('../../utils/injectIntl');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportsFilter = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        onFilterChange: React.PropTypes.func.isRequired
    },

    getInitialState: function () {
        return {
            phase: 'all'
        }
    },

    onSelect: function (e) {
        var phase = e.target.value;
        this.setState({phase: phase});
        this.props.onFilterChange({phase: phase});
    },


    render: function () {
        return (
            <Row className='show-grid filter'>
                <Col xs={1}><h4>{this.i18n('reports.filter.label')}</h4></Col>
                <Col xs={3} style={{margin: '3px 0 3px 0'}}>
                    <Input type='select' label={this.i18n('reports.table-type')} value={this.state.type}
                           labelClassName='col-xs-1 filter-label'
                           wrapperClassName='col-xs-7' title={this.i18n('reports.filter.type.tooltip')}
                           onChange={this.onSelect}>
                        <option value='all'>{this.i18n('reports.filter.type.all')}</option>
                        <option value='preliminary'>{this.i18n('reports.filter.type.preliminary')}</option>
                        <option value='investigation'>{this.i18n('investigation.type')}</option>
                    </Input>
                </Col>
            </Row>);
    }
});

module.exports = injectIntl(ReportsFilter);
