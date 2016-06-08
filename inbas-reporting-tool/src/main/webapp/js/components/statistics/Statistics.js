'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";

class Statistics extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        var i18n = this.props.i18n;
        return <Panel header={<h3>{i18n('main.statistics-nav')}</h3>} bsStyle='primary'>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(Statistics));
