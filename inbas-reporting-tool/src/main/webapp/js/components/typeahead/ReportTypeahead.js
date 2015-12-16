/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-typeahead').Typeahead;
var assign = require('object-assign');

var injectIntl = require('../../utils/injectIntl');

var Actions = require('../../actions/Actions');
var ReportStore = require('../../stores/ReportStore');
var ReportType = require('../../model/ReportType');
var TypeaheadResultList = require('./TypeaheadResultList');
var Utils = require('../../utils/Utils');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportTypeahead = React.createClass({
    propTypes: {
        name: React.PropTypes.string,
        onChange: React.PropTypes.func
    },

    mixins: [Reflux.ListenerMixin, I18nMixin],

    getInitialState: function () {
        return {
            options: []
        };
    },

    componentWillMount: function () {
        this.listenTo(ReportStore, this.onReportsLoaded);
        Actions.loadAllReports();
    },

    componentDidMount: function () {
        this.refs.reportTypeahead.focus();
    },

    onReportsLoaded: function (reports) {
        var options = [],
            option;
        for (var i = 0, len = reports.length; i < len; i++) {
            option = assign({}, reports[i].occurrence);
            option.key = reports[i].key;
            option.types = reports[i].types;
            option.type = ReportType.asString(reports[i]);
            options.push(option);
        }
        this.setState({options: options});
    },

    onOptionSelected: function (option) {
        this.props.onChange(option);
    },

    render: function () {
        var classes = {
            input: 'form-control dashboard-report-search',
            results: 'dashboard-report-search-results'
        };
        var optionLabel = function (option) {
            return option.name + ' (' + Utils.formatDate(new Date(option.startTime)) + ' - ' + option.type + ')';
        };
        return (
            <Typeahead ref='reportTypeahead' className='form-group form-group-sm' name={this.props.name}
                       formInputOption='id' placeholder={this.i18n('dashboard.search-placeholder')}
                       onOptionSelected={this.onOptionSelected} filterOption='name' displayOption={optionLabel}
                       options={this.state.options} customClasses={classes}
                       customListComponent={TypeaheadResultList}/>
        );
    }
});

module.exports = injectIntl(ReportTypeahead);
