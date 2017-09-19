import React from "react";
import ReactDOM from "react-dom";
import PropTypes from "prop-types";
import {Checkbox, ControlLabel, FormControl, FormGroup, HelpBlock, Radio} from "react-bootstrap";
import assign from "object-assign";

class Input extends React.Component {

    constructor(props) {
        super(props);
    }

    focus() {
        ReactDOM.findDOMNode(this.input).focus();
    }

    getInputDOMNode() {
        return ReactDOM.findDOMNode(this.input);
    }

    render() {
        const inputProps = assign({}, this.props);
        delete inputProps.validation;
        switch (this.props.type) {
            case 'radio':
                return this._renderRadio(inputProps);
            case 'checkbox':
                return this._renderCheckbox(inputProps);
            case 'select':
                return this._renderSelect(inputProps);
            case 'textarea':
                return this._renderTextArea(inputProps);
            default:
                return this._renderInput(inputProps);
        }
    }

    _renderCheckbox(inputProps) {
        return <Checkbox ref={c => this.input = c} {...inputProps}>{this.props.label}</Checkbox>;
    }

    _renderRadio(inputProps) {
        return <Radio ref={c => this.input = c} {...inputProps}>{this.props.label}</Radio>;
    }

    _renderSelect(inputProps) {
        return <FormGroup bsSize='small' validationState={this.props.validation}>
            {this._renderLabel()}
            <FormControl componentClass='select' ref={c => this.input = c} {...inputProps}>
                {this.props.children}
            </FormControl>
            {this.props.validation && <FormControl.Feedback/>}
            {this._renderHelp()}
        </FormGroup>;
    }

    _renderLabel() {
        return this.props.label ? <ControlLabel>{this.props.label}</ControlLabel> : null;
    }

    _renderTextArea(inputProps) {
        return <FormGroup bsSize='small' validationState={this.props.validation}>
            {this._renderLabel()}
            <FormControl componentClass='textarea' style={{height: 'auto'}} ref={c => this.input = c} {...inputProps}/>
            {this.props.validation && <FormControl.Feedback/>}
            {this._renderHelp()}
        </FormGroup>;
    }

    _renderHelp() {
        return this.props.help ? <HelpBlock>{this.props.help}</HelpBlock> : null;
    }

    _renderInput(inputProps) {
        return <FormGroup bsSize='small' validationState={this.props.validation}>
            {this._renderLabel()}
            <FormControl ref={c => this.input = c} componentClass='input' {...inputProps}/>
            {this.props.validation && <FormControl.Feedback/>}
            {this._renderHelp()}
        </FormGroup>;
    }
}

Input.propTypes = {
    type: PropTypes.string,
    label: PropTypes.string,
    value: PropTypes.any,
    onChange: PropTypes.func,
    help: PropTypes.string,
    validation: PropTypes.oneOf(['success', 'warning', 'error'])
};

Input.defaultProps = {
    type: 'text'
};

export default Input;
