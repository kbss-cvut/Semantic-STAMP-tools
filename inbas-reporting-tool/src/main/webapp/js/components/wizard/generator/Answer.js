'use strict';

import React from "react";
import assign from "object-assign";
import Typeahead from "react-bootstrap-typeahead";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Input from "../../Input";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import FormUtils from "./FormUtils";
import OptionsStore from "../../../stores/OptionsStore";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

export default class Answer extends React.Component {
    static propTypes = {
        answer: React.PropTypes.object.isRequired,
        question: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        index: React.PropTypes.number
    };

    constructor(props) {
        super(props);
        this.state = {
            options: OptionsStore.getOptions(props.question['@id'])
        }
    }

    componentWillMount() {
        var question = this.props.question;
        if (FormUtils.isTypeahead(question)) {
            if (!question[Constants.FORM.HAS_OPTION] || question[Constants.FORM.HAS_OPTION].length === 0) {
                Actions.loadOptions(question['@id']);
            } else {
                this.setState({options: Utils.processTypeaheadOptions(question[Constants.FORM.HAS_OPTION])});
            }
        }
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onOptionsLoaded = (type, options) => {
        if (type !== this.props.question['@id']) {
            return;
        }
        this.setState({options: Utils.processTypeaheadOptions(options)});
    };

    onChange = (e) => {
        var change = assign({}, this.props.answer);
        this._setValue(change, e.target.value);
        this.props.onChange(this.props.index, change);
    };

    _setValue(change, value) {
        if (this.props.answer[Constants.FORM.HAS_OBJECT_VALUE]) {
            change[Constants.FORM.HAS_OBJECT_VALUE] = {
                '@id': value
            };
        } else {
            change[Constants.FORM.HAS_DATA_VALUE] = {
                '@value': value
            };
        }
    }

    _onOptionSelected = (option) => {
        var change = assign({}, this.props.answer);
        this._setValue(change, option.id);
        this.props.onChange(this.props.index, change);
    };

    _resolveValue() {
        var answer = this.props.answer;
        if (answer[Constants.FORM.HAS_OBJECT_VALUE]) {
            return answer[Constants.FORM.HAS_OBJECT_VALUE]['@id'];
        } else {
            return Utils.getJsonAttValue(answer, Constants.FORM.HAS_DATA_VALUE);
        }
    }

    render() {
        var cls = Constants.FORM.GENERATED_ROW_SIZE === 1 ? 'col-xs-6' : 'col-xs-' + (Constants.COLUMN_COUNT / Constants.FORM.GENERATED_ROW_SIZE);
        return <div className={cls}>{this._renderInputComponent()}</div>;
    }

    _renderInputComponent() {
        var question = this.props.question,
            value = this._resolveValue(),
            label = Utils.getJsonAttValue(question, Vocabulary.RDFS_LABEL),
            title = Utils.getJsonAttValue(question, Vocabulary.RDFS_COMMENT),
            component;

        if (FormUtils.isTypeahead(question)) {
            value = Utils.idToName(this.state.options, value);
            var inputProps = {
                disabled: FormUtils.isDisabled(question)
            };
            component = <div>
                <label className='control-label'>{label}</label>
                <Typeahead className='form-group form-group-sm' formInputOption='id' inputProps={inputProps}
                           title={title} value={value} label={label} placeholder={label} filterOption='name'
                           displayOption='name' onOptionSelected={this._onOptionSelected}
                           options={this.state.options} customListComponent={TypeaheadResultList}/>
            </div>;
        } else if (Answer._hasOptions(question)) {
            component =
                <Input type='select' label={label} value={value} title={title} onChange={this.onChange}
                       disabled={FormUtils.isDisabled(question)}>
                    {this._generateSelectOptions(question[Constants.FORM.HAS_OPTION])}
                </Input>;
        } else {
            component = <Input type='text' label={label} title={title} value={value} onChange={this.onChange}
                               disabled={FormUtils.isDisabled(question)}/>;
        }
        return component;
    }

    static _hasOptions(item) {
        return item[Constants.FORM.HAS_OPTION] && item[Constants.FORM.HAS_OPTION].length !== 0;
    }

    _generateSelectOptions(options) {
        var rendered = [];
        options.sort(function (a, b) {
            var aLabel = Utils.getJsonAttValue(a, Vocabulary.RDFS_LABEL),
                bLabel = Utils.getJsonAttValue(b, Vocabulary.RDFS_LABEL);
            if (aLabel < bLabel) {
                return -1;
            }
            if (aLabel > bLabel) {
                return 1;
            }
            return 0;
        });
        for (var i = 0, len = options.length; i < len; i++) {
            rendered.push(<option value={Utils.getJsonAttValue(options[i], Vocabulary.RDFS_LABEL)}
                                  key={'opt-' + i}>{Utils.getJsonAttValue(options[i], Vocabulary.RDFS_LABEL)}</option>);
        }
        return rendered;
    }
}
