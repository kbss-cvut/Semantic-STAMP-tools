'use strict';

import React from "react";
import assign from "object-assign";
import FormTypeahead from "./FormTypeahead";
import Input from "../../Input";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import OptionsStore from "../../../stores/OptionsStore";
import Vocabulary from "../../../constants/Vocabulary";    // Perhaps we could use a standard typeahead here

export default class Question extends React.Component {
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
        if (question[Constants.LAYOUT_CLASS] && question[Constants.LAYOUT_CLASS]['@id'] === Constants.QUESTION_TYPEAHEAD) {
            Actions.loadOptions(question['@id']);
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
        this.setState({options: options});
    };

    onChange = (e) => {
        var change = assign({}, this.props.answer);
        change.textValue = e.target.value;   // TODO Determine whether this is text or code value
        this.props.onChange(this.props.index, change);
    };

    render() {
        var question = this.props.question,
            value = this.props.answer.textValue ? this.props.answer.textValue : this.props.answer.codeValue,
            label = question[Vocabulary.RDFS_LABEL],
            cls = Constants.GENERATED_ROW_SIZE === 1 ? 'col-xs-6' : 'col-xs-' + (12 / Constants.GENERATED_ROW_SIZE),
            component;

        if (this._hasOptions(question)) {
            component =
                <Input type='select' label={label} value={value} disabled={question[Constants.IS_DISABLED]}>
                    {this._generateSelectOptions(question[Constants.HAS_OPTION])}
                </Input>;
        } else if (question[Constants.LAYOUT_CLASS] && question[Constants.LAYOUT_CLASS]['@id'] === 'type-ahead') {
            component = <FormTypeahead item={question} options={this.state.options}/>;
        } else {
            component = <Input type='text' label={label} value={value}
                               disabled={question[Constants.IS_DISABLED]}/>;
        }
        return <div className={cls} key={question['@id']}>{component}</div>;
    }

    _hasOptions(item) {
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
