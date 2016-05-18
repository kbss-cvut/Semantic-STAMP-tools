'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');
var Panel = require('react-bootstrap').Panel;

var Actions = require('../../../actions/Actions');
var Constants = require('../../../constants/Constants');
var OptionsStore = require('../../../stores/OptionsStore');
var FormTypeahead = require('./FormTypeahead');
var Input = require('../../Input');
var Vocabulary = require('../../../constants/Vocabulary');

var ROW_SIZE = 1;

var GeneratedStep = React.createClass({

    mixins: [Reflux.listenTo(OptionsStore, '_onOptionsLoaded')],

    getInitialState: function () {
        var state = {},
            options = this.props.data.options;
        for (var key in options) {
            if (!options.hasOwnProperty(key)) {
                continue;
            }
            if (OptionsStore.getOptions(key)) {
                options[key] = OptionsStore.getOptions(key);
            }
        }
        state.options = options;
        return state;
    },

    _onOptionsLoaded: function (data) {
        var options = this.state.options;
        for (var key in data) {
            if (!data.hasOwnProperty(key)) {
                continue;
            }
            var values = data[key];
            for (var i = 0, len = values.length; i < len; i++) {
                var item = values[i];
                item.name = item.id.substring(item.id.lastIndexOf('/') + 1);
            }
        }
        assign(options, data);
        this.setState({options: options});
    },

    render: function () {
        return (
            <div>
                {this._generateSection(this.props.data.structure)}
            </div>
        );
    },
    _generateSection: function (structure) {
        var children = [];

        children.push(this._generateSectionQuestions(structure));
        children.push(this._generateSubsections(structure));

        var label = structure[Vocabulary.RDFS_LABEL];
        return (
            <Panel header={<h3>{label}</h3>} bsStyle='info' key={structure['@id']}>
                {children}
            </Panel>);
    },

    _generateSectionQuestions: function (section) {
        var children = [], row = [];
        if (!section[Constants.HAS_QUESTION]) {
            return children;
        }
        var questions = section[Constants.HAS_QUESTION];
        if (!Array.isArray(questions)) {
            children.push(<div className='row' key={'section-row-0'}>{this._generateQuestion(questions)}</div>);
        } else {
            for (var i = 0, len = questions.length; i < len; i++) {
                row.push(this._generateQuestion(questions[i]));
                if (row.length === ROW_SIZE) {
                    children.push(<div className='row' key={'section-row-' + i}>{row}</div>);
                    row = [];
                }
            }
            if (row.length > 0) {
                children.push(<div className='row' key={'section-row-' + i}>{row}</div>);
            }
        }
        return children;
    },

    _generateQuestion: function (item) {
        var label = item[Vocabulary.RDFS_LABEL];
        var cls = ROW_SIZE === 1 ? 'col-xs-6' : 'col-xs-' + (12 / ROW_SIZE),
            component;

        if (this._hasOptions(item)) {
            component = <Input type='select' label={label} name={item['@id']} disabled={item[Constants.IS_DISABLED]}>
                {this._generateOptions(item[Constants.HAS_OPTION])}
            </Input>;
        } else if (item[Constants.LAYOUT_CLASS] && item[Constants.LAYOUT_CLASS]['@id'] === 'type-ahead') {
            component = <FormTypeahead item={item} options={this.state.options}/>;
        } else {
            component = <Input type='text' label={label} name={item['@id']} disabled={item[Constants.IS_DISABLED]}/>;
        }
        return <div className={cls} key={item['@id']}>{component}</div>;
    },

    _hasOptions: function (item) {
        if (item[Constants.HAS_OPTION] && item[Constants.HAS_OPTION].length !== 0) {
            return true;
        }
    },

    _generateOptions: function (options) {
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
            rendered.push(<option value={options[i][Vocabulary.RDFS_LABEL]} key={'opt-' + i}>{options[i][Vocabulary.RDFS_LABEL]}</option>);
        }
        return rendered;
    },

    _generateSubsections: function (section) {
        var children = [];
        if (!section[Constants.HAS_SUBSECTION]) {
            return children;
        }
        var subsections = section[Constants.HAS_SUBSECTION];
        if (!Array.isArray(subsections)) {
            children.push(this._generateSection(subsections));
        } else {
            for (var i = 0, len = subsections.length; i < len; i++) {
                children.push(this._generateSection(subsections[i]));
            }
        }
        return children;
    }
});

module.exports = GeneratedStep;
