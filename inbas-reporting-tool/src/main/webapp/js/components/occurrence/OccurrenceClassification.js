/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-typeahead').Typeahead;
var TypeaheadResultList = require('../typeahead/EventTypeTypeaheadResultList');

var injectIntl = require('../../utils/injectIntl');

var Select = require('../Select');
var OptionsStore = require('../../stores/OptionsStore');
var TypeaheadStore = require('../../stores/TypeaheadStore');
var Utils = require('../../utils/Utils');
var I18nMixin = require('../../i18n/I18nMixin');

var OccurrenceClassification = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired
    },

    getInitialState: function () {
        return {
            severities: OptionsStore.getOccurrenceSeverityOptions(),
            occurrenceCategories: TypeaheadStore.getOccurrenceCategories()
        };
    },

    componentDidMount: function () {
        this.listenTo(OptionsStore, this.onSeveritiesLoaded);
        this.listenTo(TypeaheadStore, this.onOccurrenceCategoriesLoaded)
    },

    onSeveritiesLoaded: function (type, data) {
        if (type !== 'occurrenceSeverity') {
            return;
        }
        this.setState({severities: data});
    },

    onOccurrenceCategoriesLoaded: function () {
        this.setState({occurrenceCategories: TypeaheadStore.getOccurrenceCategories()});
    },

    onChange: function (e) {
        var value = e.target.value;
        this.props.onChange(e.target.name, value);
    },

    onCategorySelect: function (cat) {
        this.props.onChange('occurrenceCategory', cat);
    },

    _prepareOptions: function () {
        var options = this.state.severities,
            toRender = [];
        for (var i = 0, len = options.length; i < len; i++) {
            toRender.push({label: Utils.constantToString(options[i], true), value: options[i]});
        }
        return toRender;
    },

    render: function () {
        var classes = {
                input: 'form-control'
            },
            report = this.props.report;
        return (
            <div className='row'>
                <div className='col-xs-4'>
                    <Select label={this.i18n('occurrence.class') + '*'} name='severityAssessment'
                            title={this.i18n('occurrence.class-tooltip')}
                            value={report.severityAssessment} options={this._prepareOptions()}
                            onChange={this.onChange}/>
                </div>
                <div className='col-xs-4'>
                    <label className='control-label'>
                        {this.i18n('preliminary.detail.occurrence-category.label') + '*'}
                    </label>
                    <Typeahead className='form-group form-group-sm' name='occurrenceCategory'
                               formInputOption='id'
                               placeholder={this.i18n('preliminary.detail.occurrence-category.label')}
                               onOptionSelected={this.onCategorySelect} filterOption='name'
                               value={report.occurrenceCategory ? report.occurrenceCategory.name : null}
                               displayOption='name' options={this.state.occurrenceCategories}
                               customClasses={classes} customListComponent={TypeaheadResultList}/>
                </div>
            </div>
        )
    }
});

module.exports = injectIntl(OccurrenceClassification);
