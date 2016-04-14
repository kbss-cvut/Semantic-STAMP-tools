'use strict';

import React from "react";

const I18nWrapper = (Component) => class extends React.Component {
    constructor(props) {
        super(props);
    }

    i18n = (id) => {
        return this.props.intl.messages[id];
    }

    render() {
        return <Component i18n={this.i18n} {...this.props}/>;
    }
};

export default I18nWrapper;
