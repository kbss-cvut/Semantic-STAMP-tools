import React from "react";
import PropTypes from "prop-types";
import Typeahead from "react-bootstrap-typeahead";
import JsonLdUtils from "jsonld-utils";

import Constants from "../../../constants/Constants";
import ExternalLink from "../../misc/ExternalLink";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import OptionsStore from "../../../stores/OptionsStore";
import Select from "../../Select";
import EventTypeTypeaheadResultList from "../../typeahead/EventTypeTypeaheadResultList";
import Vocabulary from "../../../constants/Vocabulary";


class OccurrenceClassification extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            occurrenceClasses: OptionsStore.getOptions(Constants.OPTIONS.OCCURRENCE_CLASS),
            occurrenceCategories: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions(Constants.OPTIONS.OCCURRENCE_CATEGORY))
        };
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type, data) => {
        if (type === Constants.OPTIONS.OCCURRENCE_CLASS) {
            this.setState({occurrenceClasses: data});
        } else if (type === Constants.OPTIONS.OCCURRENCE_CATEGORY) {
            this.setState({occurrenceCategories: JsonLdUtils.processTypeaheadOptions(data)});
            const selected = this._resolveSelectedCategory();
            if (selected) {
                this.occurrenceCategory.selectOption(selected);
            }
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _transformOccurrenceClasses() {
        return this.state.occurrenceClasses.map((item) => {
            return {
                value: item['@id'],
                label: JsonLdUtils.getJsonAttValue(item, Vocabulary.RDFS_LABEL),
                title: JsonLdUtils.getJsonAttValue(item, Vocabulary.RDFS_COMMENT)
            };
        });
    }

    onChange = (e) => {
        const change = {};
        change[e.target.name] = e.target.value;
        this.props.onChange(change);
    };

    onCategorySelect = (cat) => {
        const occurrence = this.props.report.occurrence;
        occurrence.eventType = cat.id;
        this.props.onChange({'occurrence': occurrence});
    };

    render() {
        const report = this.props.report;
        return <div className='row'>
            <div className='col-xs-4'>
                <Select label={this.i18n('occurrence.class') + '*'} name='severityAssessment'
                        title={this.i18n('occurrence.class-tooltip')} addDefault={true}
                        value={report.severityAssessment} options={this._transformOccurrenceClasses()}
                        onChange={this.onChange}/>
            </div>
            <div className='col-xs-4'>
                <Typeahead name='occurrenceCategory' label={this.i18n('report.occurrence.category.label') + '*'}
                           ref={c => this.occurrenceCategory = c} formInputOption='id' optionsButton={true}
                           placeholder={this.i18n('report.occurrence.category.label')}
                           onOptionSelected={this.onCategorySelect} filterOption='name'
                           value={this._resolveCategoryValue()} size='small'
                           displayOption='name' options={this.state.occurrenceCategories}
                           customListComponent={EventTypeTypeaheadResultList}/>
            </div>
            {this._renderCategoryLink()}
        </div>;
    }

    _resolveCategoryValue() {
        const cat = this._resolveSelectedCategory();
        return cat ? cat.name : '';
    }

    _resolveSelectedCategory() {
        const catId = this.props.report.occurrence.eventType,
            categories = this.state.occurrenceCategories;
        return categories.find(function (item) {
            return item.id === catId;
        });
    }

    _renderCategoryLink() {
        const cat = this.props.report.occurrence.eventType;
        return cat ?
            <div className='col-xs-1'><ExternalLink url={cat} title={this._resolveCategoryValue() + '\n' + cat}/>
            </div> : null;
    }
}

OccurrenceClassification.propTypes = {
    report: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
};

export default injectIntl(I18nWrapper(OccurrenceClassification));
