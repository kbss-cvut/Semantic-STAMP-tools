'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Glyphicon = require('react-bootstrap').Glyphicon;
var classNames = require('classnames');
var Reflux = require('reflux');
var Typeahead = require('react-bootstrap-typeahead');
var TypeaheadResultList = require('../../typeahead/EventTypeTypeaheadResultList').default;
var JsonLdUtils = require('jsonld-utils').default;

var injectIntl = require('../../../utils/injectIntl');

var Actions = require('../../../actions/Actions');
var Select = require('../../Select');
var OptionsStore = require('../../../stores/OptionsStore');
var TypeaheadStore = require('../../../stores/TypeaheadStore');
var I18nMixin = require('../../../i18n/I18nMixin');
var Constants = require('../../../constants/Constants');
var Vocabulary = require('../../../constants/Vocabulary');
var ExternalLink = require('../../misc/ExternalLink').default;

var OccurrenceClassification = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    },

    getInitialState: function () {
        return {
            occurrenceClasses: OptionsStore.getOptions(Constants.OPTIONS.OCCURRENCE_CLASS),
            occurrenceCategories: JsonLdUtils.processTypeaheadOptions(TypeaheadStore.getOccurrenceCategories()),
            secondaryCategory: this.props.report.occurrence.eventTypes.length > 1
        };
    },

    componentDidMount: function () {
        this.listenTo(OptionsStore, this.onOccurrenceClassesLoaded);
        this.listenTo(TypeaheadStore, this.onOccurrenceCategoriesLoaded)
    },

    onOccurrenceClassesLoaded: function (type, data) {
        if (type !== Constants.OPTIONS.OCCURRENCE_CLASS) {
            return;
        }
        this.setState({occurrenceClasses: data});
    },

    _transformOccurrenceClasses: function () {
        return this.state.occurrenceClasses.map((item) => {
            return {
                value: item['@id'],
                label: JsonLdUtils.getJsonAttValue(item, Vocabulary.RDFS_LABEL),
                title: JsonLdUtils.getJsonAttValue(item, Vocabulary.RDFS_COMMENT)
            };
        });
    },

    onOccurrenceCategoriesLoaded: function (data) {
        if (data.action !== Actions.loadOccurrenceCategories) {
            return;
        }
        var options = data.data,
            report = this.props.report,
            selected;
        this.setState({occurrenceCategories: JsonLdUtils.processTypeaheadOptions(options)});
        if (report.occurrence.eventTypes.length > 0) {
            selected = this._resolveSelectedCategory(report.occurrence.eventTypes[0]);
            if (selected) {
                this.primaryCategory.selectOption(selected);
            }
            if (report.occurrence.eventTypes.length > 1) {
                selected = this._resolveSelectedCategory(report.occurrence.eventTypes[1]);
                if (selected) {
                    this.secondaryCategory.selectOption(selected);
                }
            }
        }
    },

    onChange: function (e) {
        var change = {};
        change[e.target.name] = e.target.value;
        this.props.onChange(change);
    },

    onCategorySelect: function (cat, index) {
        var occurrence = this.props.report.occurrence;
        occurrence.eventTypes[index] = cat.id;
        this.props.onChange({'occurrence': occurrence});
    },

    _addSecondaryCategory: function () {
        this.setState({secondaryCategory: true});
    },

    _removeSecondaryCategory: function () {
        var occurrence = this.props.report.occurrence;
        occurrence.eventTypes.pop();
        this.props.onChange({'occurrence': occurrence});
        this.setState({secondaryCategory: false});
    },

    render: function () {
        var report = this.props.report;
        return <div className='row'>
            <div className='col-xs-4'>
                <Select label={this.i18n('occurrence.class') + '*'} name='severityAssessment'
                        title={this.i18n('occurrence.class-tooltip')} addDefault={true}
                        value={report.severityAssessment} options={this._transformOccurrenceClasses()}
                        onChange={this.onChange}/>
            </div>
            <div className='col-xs-4'>
                {this._renderPrimaryCategory()}
            </div>
            <div className='col-xs-4'>
                {this._renderSecondaryCategory()}
            </div>
        </div>;
    },

    _renderPrimaryCategory: function () {
        var report = this.props.report,
            category = report.occurrence.eventTypes.length > 0 ? report.occurrence.eventTypes[0] : null,
            columnClass = classNames({'col-xs-11': category !== null, 'col-xs-12': category === null});

        return <div className='row'>
            <div className={columnClass}>
                <Typeahead label={this.i18n('report.occurrence.category.label') + '*'}
                           ref={c => this.primaryCategory = c}
                           formInputOption='id' optionsButton={true}
                           placeholder={this.i18n('report.occurrence.category.label')}
                           onOptionSelected={opt => this.onCategorySelect(opt, 0)} filterOption='name'
                           value={this._resolveCategoryValue(category)} size='small'
                           displayOption='name' options={this.state.occurrenceCategories}
                           customListComponent={TypeaheadResultList}/>
            </div>
            {this._renderCategoryLink(category)}
        </div>;
    },

    _resolveCategoryValue: function (category) {
        var cat = this._resolveSelectedCategory(category);
        return cat ? cat.name : '';
    },

    _resolveSelectedCategory: function (categoryId) {
        var categories = this.state.occurrenceCategories;
        return categories.find(function (item) {
            return item.id === categoryId;
        });
    },

    _renderCategoryLink: function (categoryId) {
        return categoryId ? <div className='col-xs-1'>
            <ExternalLink url={categoryId} title={this._resolveCategoryValue(categoryId) + '\n' + categoryId}/>
        </div> : null;
    },

    _renderSecondaryCategory: function () {
        var occurrence = this.props.report.occurrence;
        if (occurrence.eventTypes.length < 1) {
            return null;
        }
        if (occurrence.eventTypes.length === 1 && !this.state.secondaryCategory) {
            return <Button bsStyle='primary' bsSize='small' onClick={this._addSecondaryCategory}
                           className='in-input-line'>
                <Glyphicon glyph='plus' className='add-icon-glyph'/>
                {this.i18n('occurrence.add-category')}
            </Button>;
        } else {
            return this._renderSecondaryCategoryTypeahead();
        }
    },

    _renderSecondaryCategoryTypeahead: function () {
        var occurrence = this.props.report.occurrence,
            category = occurrence.eventTypes.length > 1 ? occurrence.eventTypes[1] : null,
            columnClass = classNames({'col-xs-10': category !== null, 'col-xs-12': category === null});

        return <div className='row'>
            <div className={columnClass}>
                <Typeahead label={this.i18n('report.occurrence.category.label')}
                           ref={c => this.secondaryCategory = c}
                           formInputOption='id' optionsButton={true}
                           placeholder={this.i18n('report.occurrence.category.label')}
                           onOptionSelected={opt => this.onCategorySelect(opt, 1)} filterOption='name'
                           value={this._resolveCategoryValue(category)} size='small'
                           displayOption='name' options={this.state.occurrenceCategories}
                           customListComponent={TypeaheadResultList}/>
            </div>
            {this._renderCategoryLink(category)}
            {this._renderRemoveCategoryButton(category)}
        </div>
    },

    _renderRemoveCategoryButton: function (category) {
        return category ? <div className='col-xs-1'>
            <Button bsStyle='warning' bsSize='small' className='in-input-line remove-secondary-category'
                    title={this.i18n('occurrence.remove-category-tooltip')} onClick={this._removeSecondaryCategory}>
                <Glyphicon glyph='remove'/>
            </Button>
        </div> : null;
    }
});

module.exports = injectIntl(OccurrenceClassification);
