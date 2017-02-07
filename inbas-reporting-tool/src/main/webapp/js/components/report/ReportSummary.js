'use strict';

import React from "react";
import {ContentState, Editor, EditorState, RichUtils} from "draft-js";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";

class ReportSummary extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        const content = props.report.summary ? ContentState.createFromText(props.report.summary) : null;
        this.state = {
            editorState: content ? EditorState.createWithContent(content) : EditorState.createEmpty()
        };
    }

    _onChange = (newState) => {
        this.setState({editorState: newState});
        this.props.onChange({summary: newState.getCurrentContent().getPlainText()});
    };

    _focus = () => {
        this.editor.focus();
    };

    render() {
        return <div className='form-group'>
            <label className='control-label'>{this.i18n('narrative') + '*'}</label>
            <div className='rich-editor form-control' onClick={this._focus}
                 title={this.i18n('report.narrative-tooltip')}>
                <Editor ref={c => this.editor = c} editorState={this.state.editorState} onChange={this._onChange}
                        placeholder={this.i18n('narrative')}/>
            </div>
        </div>;
    }
}

export default injectIntl(I18nWrapper(ReportSummary));
