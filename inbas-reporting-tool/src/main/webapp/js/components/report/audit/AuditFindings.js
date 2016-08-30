'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";

class AuditFindings extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showWindow: false
        };
    }

    render() {
        return <Panel header={<h5>{this.i18n('audit.findings.header')}</h5>} bsStyle='info'>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(AuditFindings));
