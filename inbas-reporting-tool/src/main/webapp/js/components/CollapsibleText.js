/**
 * Created by ledvima1 on 30.6.15.
 */

'use strict';

var React = require('react');
var CollapsibleMixin = require('react-bootstrap').CollapsibleMixin;
var classNames = require('classnames');

var DEFAULT_THRESHOLD = 100;

var CollapsibleText = React.createClass({
    mixins: [CollapsibleMixin],

    getCollapsibleDOMNode: function () {
        return React.findDOMNode(this.refs.panel);
    },

    getCollapsibleDimensionValue: function () {
        return React.findDOMNode(this.refs.panel).scrollHeight;
    },

    onHandleToggle: function (e) {
        e.preventDefault();
        this.setState({expanded: !this.state.expanded});
    },

    getTextPreview: function () {
        var threshold = this.props.maxLength ? this.props.maxLength : DEFAULT_THRESHOLD;
        if (!this.props.text) {
            return '';
        }
        return this.props.text.length > threshold ? (this.props.text.substring(0, threshold) + '...') : this.props.text;
    },

    renderTextPreview: function () {
        if (this.state.expanded) {
            return null;
        }
        var text = this.getTextPreview();
        return (
            <div onClick={this.onHandleToggle} title={this.props.text}>
                {text}
            </div>
        );
    },

    render: function () {
        var styles = this.getCollapsibleClassSet();
        var textPreview = this.renderTextPreview();
        return (
            <div>
                {textPreview}
                <div ref='panel' className={classNames(styles)} onClick={this.onHandleToggle} title={this.props.text}>
                    {this.props.text}
                </div>
            </div>
        );

    }
});

module.exports = CollapsibleText;
