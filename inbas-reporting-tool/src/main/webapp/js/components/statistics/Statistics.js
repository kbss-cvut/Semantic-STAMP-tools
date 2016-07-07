'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import Constants from "../../constants/Constants";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";

const Statistics = (props) => {
    var i18n = props.i18n;
    return <Panel header={<h3>{i18n('main.statistics-nav')}</h3>} bsStyle='primary'>
        <iframe className='statistics-frame' src={Constants.STATISTICS_URL} allowFullScreen='true'></iframe>
    </Panel>;
};

export default injectIntl(I18nWrapper(Statistics));
