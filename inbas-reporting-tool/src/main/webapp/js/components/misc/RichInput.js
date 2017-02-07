'use strict';

import React from "react";
import classNames from "classnames";
import {ContentState, convertFromHTML, Editor, EditorState, RichUtils} from "draft-js";
import {stateToHTML} from "draft-js-export-html";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";

const EXPORT_CONFIG = {
    inlineStyles: {
        BOLD: {element: 'b'},
        UNDERLINE: {
            style: {
                textDecoration: 'underline'
            }
        }
    }
};

class RichInput extends React.Component {
    static propTypes = {
        value: React.PropTypes.string,
        label: React.PropTypes.string,
        title: React.PropTypes.string,
        placeholder: React.PropTypes.string,
        onChange: React.PropTypes.func.isRequired
    };

    static defaultProps = {
        value: '',
        placeholder: '',
        title: ''
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        const blocks = convertFromHTML(props.value),
            content = blocks ? ContentState.createFromBlockArray(blocks.contentBlocks, blocks.entityMap)
                : ContentState.createFromText('');
        this.state = {
            editorState: EditorState.createWithContent(content)
        };
    }

    _onChange = (newState) => {
        this.setState({editorState: newState});
        this.props.onChange(stateToHTML(newState.getCurrentContent(), EXPORT_CONFIG));
    };

    _focus = () => {
        this.editor.focus();
    };

    _toggleBlockType = (blockType) => {
        this._onChange(RichUtils.toggleBlockType(this.state.editorState, blockType));
    };

    _toggleInlineStyle = (inlineStyle) => {
        this._onChange(RichUtils.toggleInlineStyle(this.state.editorState, inlineStyle));
    };

    _handleKeyCommand = (command) => {
        const {editorState} = this.state;
        const newState = RichUtils.handleKeyCommand(editorState, command);
        if (newState) {
            this._onChange(newState);
            return true;
        }
        return false;
    };

    render() {
        const {editorState} = this.state,
            contentState = editorState.getCurrentContent();

        // If the user changes block type before entering any text, we can
        // either style the placeholder or hide it. Let's just hide it now.
        let hidePlaceholder = !contentState.hasText() && contentState.getBlockMap().first().getType() !== 'unstyled',
            className = classNames('rich-editor', {'rich-editor-hide-placeholder': hidePlaceholder});

        return <div className='form-group'>
            {this.props.label && <label className='control-label'>{this.props.label}</label>}
            <div className='rich-editor-container'>
                <BlockStyleControls editorState={editorState} onToggle={this._toggleBlockType}/>
                <InlineStyleControls editorState={editorState} onToggle={this._toggleInlineStyle}/>

                <div className={className} onClick={this._focus} title={this.props.title}>
                    <Editor ref={c => this.editor = c} editorState={this.state.editorState}
                            handleKeyCommand={this._handleKeyCommand}
                            onChange={this._onChange} placeholder={this.props.placeholder} spellCheck={true}/>
                </div>
            </div>
        </div>;
    }
}

class StyleButton extends React.Component {
    constructor(props) {
        super(props);
    }

    _onToggle = (e) => {
        e.preventDefault();
        this.props.onToggle(this.props.style);
    };

    render() {
        let className = classNames('rich-editor-style-button', {'rich-editor-active-button': this.props.active});

        return <span className={className} onMouseDown={this._onToggle}>
              {this.props.label}
        </span>;
    }
}

const BLOCK_TYPES = [
    {label: 'editor.rich.h1', style: 'header-one'},
    {label: 'editor.rich.h2', style: 'header-two'},
    {label: 'editor.rich.h3', style: 'header-three'},
    {label: 'editor.rich.h4', style: 'header-four'},
    {label: 'editor.rich.h5', style: 'header-five'},
    {label: 'editor.rich.h6', style: 'header-six'},
    {label: 'editor.rich.blockquote', style: 'blockquote'},
    {label: 'editor.rich.ul', style: 'unordered-list-item'},
    {label: 'editor.rich.ol', style: 'ordered-list-item'}
];

let BlockStyleControls = (props) => {
    const {editorState} = props,
        selection = editorState.getSelection(),
        blockType = editorState.getCurrentContent().getBlockForKey(selection.getStartKey()).getType();

    return <div className="rich-editor-controls">
        {BLOCK_TYPES.map((type) =>
            <StyleButton key={type.label} active={type.style === blockType} label={props.i18n(type.label)}
                         onToggle={props.onToggle} style={type.style}/>
        )}
    </div>;
};

BlockStyleControls = injectIntl(I18nWrapper(BlockStyleControls));

const INLINE_STYLES = [
    {label: 'editor.rich.bold', style: 'BOLD'},
    {label: 'editor.rich.italic', style: 'ITALIC'},
    {label: 'editor.rich.underline', style: 'UNDERLINE'}
];

let InlineStyleControls = (props) => {
    const currentStyle = props.editorState.getCurrentInlineStyle();
    return <div className="rich-editor-controls">
        {INLINE_STYLES.map(type =>
            <StyleButton key={type.label} active={currentStyle.has(type.style)} label={props.i18n(type.label)}
                         onToggle={props.onToggle} style={type.style}/>
        )}
    </div>;
};

InlineStyleControls = injectIntl(I18nWrapper(InlineStyleControls));

export default injectIntl(I18nWrapper(RichInput));
