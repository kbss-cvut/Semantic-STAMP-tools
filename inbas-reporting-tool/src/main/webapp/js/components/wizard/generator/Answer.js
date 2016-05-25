'use strict';

import React from "react";
import assign from "object-assign";
import Typeahead from "react-bootstrap-typeahead";
import TypeaheadResultList from "../../typeahead/TypeaheadResultList";
import Input from "../../Input";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
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
        if (Answer._isTypeahead(question)) {
            if (!question[Constants.HAS_OPTION] || question[Constants.HAS_OPTION].length === 0) {
                Actions.loadOptions(question['@id']);
            } else {
                this.setState({options: Utils.processTypeaheadOptions(question[Constants.HAS_OPTION])});
            }
        }
    }

    static _isTypeahead(question) {
        return Utils.hasValue(question, Constants.LAYOUT_CLASS, Constants.QUESTION_TYPEAHEAD);
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
        change['@value'] = e.target.value;
        this.props.onChange(this.props.index, change);
    };

    _onOptionSelected = (option) => {
        var change = assign({}, this.props.answer);
        change['@value'] = option.id;
        this.props.onChange(this.props.index, change);
    };

    render() {
        var cls = Constants.GENERATED_ROW_SIZE === 1 ? 'col-xs-6' : 'col-xs-' + (Constants.COLUMN_COUNT / Constants.GENERATED_ROW_SIZE);
        return <div className={cls}>{this._renderInputComponent()}</div>;
    }

    _renderInputComponent() {
        var question = this.props.question,
            value = this.props.answer['@value'],
            label = question[Vocabulary.RDFS_LABEL],
            title = question[Vocabulary.RDFS_COMMENT] ? question[Vocabulary.RDFS_COMMENT] : null,
            component;

        if (Answer._isTypeahead(question)) {
            value = Utils.idToName(this.state.options, value);
            component = <div>
                <label className='control-label'>{label}</label>
                <Typeahead className='form-group form-group-sm' formInputOption='id'
                           title={title} value={value} label={label} placeholder={label} filterOption='name'
                           displayOption='name' onOptionSelected={this._onOptionSelected}
                           options={this.state.options} customListComponent={TypeaheadResultList}/>
            </div>;
        } else if (Answer._hasOptions(question)) {
            component =
                <Input type='select' label={label} value={value} title={title} onChange={this.onChange}
                       disabled={question[Constants.IS_DISABLED]}>
                    {this._generateSelectOptions(question[Constants.HAS_OPTION])}
                </Input>;
        } else {
            component = <Input type='text' label={label} title={title} value={value} onChange={this.onChange}
                               disabled={question[Constants.IS_DISABLED]}/>;
        }
        return component;
    }

    static _hasOptions(item) {
        if (item[Constants.HAS_OPTION] && item[Constants.HAS_OPTION].length !== 0) {
            return true;
        }
    }

    _generateSelectOptions(options) {
        var rendered = [];
        options.sort(function (a, b) {
            if (a[Vocabulary.RDFS_LABEL] < b[Vocabulary.RDFS_LABEL]) {
                return -1;
            }
            if (a[Vocabulary.RDFS_LABEL] > b[Vocabulary.RDFS_LABEL]) {
                return 1;
            }
            return 0;
        });
        for (var i = 0, len = options.length; i < len; i++) {
            rendered.push(<option value={options[i][Vocabulary.RDFS_LABEL]}
                                  key={'opt-' + i}>{options[i][Vocabulary.RDFS_LABEL]}</option>);
        }
        return rendered;
    }
}
