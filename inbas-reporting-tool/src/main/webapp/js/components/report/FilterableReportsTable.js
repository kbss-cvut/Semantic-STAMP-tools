'use strict';

var React = require('react');
var Reflux = require('reflux');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var JsonLdUtils = require('jsonld-utils').default;
var assign = require('object-assign');

var Constants = require('../../constants/Constants');
var injectIntl = require('../../utils/injectIntl');
var I18nMixin = require('../../i18n/I18nMixin');
var OptionsStore = require('../../stores/OptionsStore');
var ReportsTable = require('./ReportsTable');
var ReportType = require('../../model/ReportType');
var Select = require('../Select');
var Vocabulary = require('../../constants/Vocabulary');

var FilterableReportsTable = React.createClass({
    mixins: [I18nMixin, Reflux.listenTo(OptionsStore, '_onPhasesLoaded')],

    propTypes: {
        allReports: React.PropTypes.array,
        reports: React.PropTypes.array,
        actions: React.PropTypes.object.isRequired,
        sort: React.PropTypes.object,
        filter: React.PropTypes.object
    },

    getInitialState: function () {
        var filterInit = this.props.filter ? this.props.filter : {};
        return {
            phase: filterInit['phase'] ? filterInit['phase'] : Constants.FILTER_DEFAULT,
            types: filterInit['types'] ? filterInit['types'] : Constants.FILTER_DEFAULT
        }
    },

    _onPhasesLoaded(type) {
        if (type === 'reportingPhase') {
            this.forceUpdate();
        }
    },

    onSelect: function (e) {
        var value = e.target.value;
        var change = {};
        change[e.target.name] = value;
        this.setState(change);
        this.props.actions.onFilterChange(change);
    },

    _onTypeToggle: function (type) {
        var change = {};
        if (type !== Constants.FILTER_DEFAULT) {
            var origTypes = Array.isArray(this.state.types) ? this.state.types.slice() : [this.state.types];
            if (origTypes.indexOf(type) === -1) {   // Add type to filter
                change.types = [type].concat(origTypes);
                if (change.types.indexOf(Constants.FILTER_DEFAULT) !== -1) {
                    change.types.splice(change.types.indexOf(Constants.FILTER_DEFAULT), 1);
                }
            } else {    // Remove type from filter
                origTypes.splice(origTypes.indexOf(type), 1);
                change.types = origTypes;
            }
            if (change.types.length === 0) {    // If no filters left, reset back to All
                change.types = Constants.FILTER_DEFAULT;
            }
        } else {
            change.types = Constants.FILTER_DEFAULT;
        }
        this.setState(change);
        this.props.actions.onFilterChange(change);
    },

    onResetFilters: function () {
        var newState = {};
        Object.getOwnPropertyNames(this.state).forEach((key) => {
            newState[key] = Constants.FILTER_DEFAULT;
        });
        this.setState(newState);
        this.props.actions.onFilterChange(newState);
    },


    render: function () {
        return <div>
            {this._renderReportTypeFilters()}
            <ReportsTable {...this.props} children={this._renderFiltersInTable()}/>
        </div>;
    },

    _renderReportTypeFilters: function () {
        var types = this._getReportTypes();
        if (types.length <= 1) {
            return null;
        }
        return <div className='row'>
            <div className='col-xs-1'>
                <label className='control-label type-filters-label'>{this.i18n('reports.filter.type.label')}</label>
            </div>
            <div className='col-xs-11'>
                {this._renderReportTypeFilterButtons(types)}
            </div>
        </div>;
    },

    _renderReportTypeFilterButtons: function (types) {
        var type,
            buttons = [],
            filteredTypes = this.state['types'], i18n = this.i18n;
        buttons.push(<Button key={Constants.FILTER_DEFAULT} bsSize='small'
                             bsStyle={filteredTypes.indexOf(Constants.FILTER_DEFAULT) !== -1 ? 'primary' : 'default'}
                             onClick={() => this._onTypeToggle(Constants.FILTER_DEFAULT)}>{this.i18n('reports.filter.type.all')}</Button>);
        for (var i = 0, len = types.length; i < len; i++) {
            type = types[i];
            ((type) => {
                buttons.push(<Button key={type.type} onClick={() => this._onTypeToggle(type.type)} bsSize='small'
                                     bsStyle={filteredTypes.indexOf(type.type) !== -1 ? 'primary' : 'default'}>{i18n(type.label)}</Button>);
            })(type);
        }
        return <ButtonToolbar className='type-filters'>
            {buttons}
        </ButtonToolbar>;
    },

    _renderFiltersInTable: function () {
        return <tr className='filter'>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>{this._renderSelect('phase', this.state.phase, this._getReportingPhaseOptions())}</td>
            <td >
                <Button bsStyle='primary' bsSize='small' className='reset-filters'
                        onClick={this.onResetFilters}>{this.i18n('reports.filter.reset')}</Button>
            </td>
        </tr>;
    },

    _renderSelect: function (name, value, options) {
        options.unshift(this._allFilterOption());
        return <Select name={name} value={value} options={options} onChange={this.onSelect}/>;
    },

    _getReportTypes: function () {
        var types = [],
            typeSet = [],
            reportTypes,
            reports = this.props.allReports;
        for (var i = 0, len = reports.length; i < len; i++) {
            reportTypes = reports[i].types;
            if (!reportTypes || reportTypes.length === 0) {
                continue;
            }
            for (var j = 0, lenn = reportTypes.length; j < lenn; j++) {
                var typeLabel = ReportType.getTypeLabel(reportTypes[j]);
                if (typeLabel && typeSet.indexOf(reportTypes[j]) === -1) {
                    typeSet.push(reportTypes[j]);
                    types.push({type: reportTypes[j], label: typeLabel});
                }
            }
        }
        types.sort((a, b) => a.label.localeCompare(b.label));   // Sort types by label
        return types;
    },

    _allFilterOption: function () {
        return {value: Constants.FILTER_DEFAULT, label: this.i18n('reports.filter.type.all')};
    },

    _getReportingPhaseOptions: function () {
        var phases = OptionsStore.getOptions('reportingPhase'),
            options = [];
        for (var i = 0, len = phases.length; i < len; i++) {
            options.push({
                value: phases[i]['@id'],
                label: JsonLdUtils.getLocalized(phases[i][Vocabulary.RDFS_LABEL], this.props.intl)
            });
        }
        return options;
    }
});

module.exports = injectIntl(FilterableReportsTable);
