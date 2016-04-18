/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var Typeahead = require('react-bootstrap-typeahead');
var TypeaheadResultList = require('../../typeahead/EventTypeTypeaheadResultList');

var injectIntl = require('../../../utils/injectIntl');

var Select = require('../../Select');
var OptionsStore = require('../../../stores/OptionsStore');
var TypeaheadStore = require('../../../stores/TypeaheadStore');
var Utils = require('../../../utils/Utils');
var I18nMixin = require('../../../i18n/I18nMixin');
var Vocabulary = require('../../../constants/Vocabulary');

var OccurrenceClassification = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired
    },

    getInitialState: function () {
        var options = TypeaheadStore.getOccurrenceCategories();
        return {
            severities: OptionsStore.getOccurrenceSeverityOptions(),
            occurrenceCategories: options.length !== 0 ? this._transformOccurrenceCategories(options) : options
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
        var options = TypeaheadStore.getOccurrenceCategories();
        options = this._transformOccurrenceCategories(options);
        this.setState({occurrenceCategories: options});
    },

    _transformOccurrenceCategories: function (options) {
        return options.map(function (item) {
            return {
                id: item['@id'],
                name: item[Vocabulary.RDFS_LABEL],
                description: item[Vocabulary.RDFS_COMMENT]
            };
        });
    },

    onChange: function (e) {
        var value = e.target.value;
        this.props.onChange(e.target.name, value);
    },

    onCategorySelect: function (cat) {
        this.props.onChange('occurrenceCategory', cat);
    },

    _onShowCategories: function () {
        this.refs.occurrenceCategory.showOptions();
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
                        {this.i18n('report.occurrence.category.label') + '*'}
                    </label>
                    <Typeahead className='form-group form-group-sm' name='occurrenceCategory'
                               ref='occurrenceCategory' formInputOption='id' optionsButton={true}
                               placeholder={this.i18n('report.occurrence.category.label')}
                               onOptionSelected={this.onCategorySelect} filterOption='name'
                               value={report.occurrenceCategory ? report.occurrenceCategory.name : ''}
                               displayOption='name' options={this.state.occurrenceCategories}
                               customClasses={classes} customListComponent={TypeaheadResultList}/>
                </div>
            </div>
        )
    }
});

module.exports = injectIntl(OccurrenceClassification);
