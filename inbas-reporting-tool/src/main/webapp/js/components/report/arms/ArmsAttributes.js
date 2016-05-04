'use strict';

import React from "react";
import injectIntl from "../../../utils/injectIntl";
import I18nWrapper from "../../../i18n/I18nWrapper";

class ArmsAttributes extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };
    
    constructor(props) {
        super(props);
    }
}

export default injectIntl(I18nWrapper(ArmsAttributes));
