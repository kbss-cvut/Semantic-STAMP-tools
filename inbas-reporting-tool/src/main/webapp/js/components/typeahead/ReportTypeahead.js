/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-typeahead').Typeahead;
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/ReportsStore');
var TypeaheadResultList = require('./TypeaheadResultList');
var Utils = require('../../utils/Utils');

var ReportTypeahead = React.createClass({
    propTypes: {
        name: React.PropTypes.string,
        onChange: React.PropTypes.func
    },

    mixins: [Reflux.ListenerMixin],

    getInitialState: function () {
        return {
            options: []
        };
    },

    componentWillMount: function () {
        this.listenTo(ReportsStore, this.onReportsLoaded);
        Actions.loadReports();
    },

    componentDidMount: function () {
        this.refs.reportTypeahead.focus();
    },

    onReportsLoaded: function () {
        var options = [],
            reports = ReportsStore.getReports(),
            option;
        for (var i = 0, len = reports.length; i < len; i++) {
            option = assign({}, reports[i].occurrence);
            option.reportKey = reports[i].key;
            options.push(option);
        }
        this.setState({options: options});
    },

    onOptionSelected: function (option) {
        this.props.onChange(option.reportKey);
    },

    render: function () {
        var classes = {
            input: 'form-control dashboard-report-search',
            results: 'dashboard-report-search-results'
        };
        var optionLabel = function (option) {
            return option.name + ' (' + Utils.formatDate(new Date(option.startTime)) + ')';
        };
        return (
            <Typeahead ref='reportTypeahead' className='form-group form-group-sm' name={this.props.name}
                       formInputOption='id' placeholder='Occurrence Summary'
                       onOptionSelected={this.onOptionSelected} filterOption='name' displayOption={optionLabel}
                       options={this.state.options} customClasses={classes}
                       customListComponent={TypeaheadResultList}/>
        );
    }
});

module.exports = ReportTypeahead;
