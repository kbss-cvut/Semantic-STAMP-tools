'use strict';

import React from "react";
import {Button, Panel} from "react-bootstrap";
import Constants from "../../constants/Constants";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";

class Statistics extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            type: 'general'
        }
    }

    _onTypeSelect = (type) => {
        this.setState({type: type});
    };

    render() {
        return <Panel header={<h3>{this.i18n('main.statistics-nav')}</h3>} bsStyle='primary'>
            <div className='statistics-switchers content-center'>
                {this._renderStatisticsTypeButtons()}
            </div>
            <iframe className='statistics-frame' src={Constants.STATISTICS[this.state.type]}
                    allowFullScreen='true'></iframe>
        </Panel>;
    }

    _renderStatisticsTypeButtons() {
        var buttons = [], type;
        Object.getOwnPropertyNames(Constants.STATISTICS).forEach(type => {
            buttons.push(<Button key={type} bsStyle={this.state.type === type ? 'primary' : 'default'}
                                 onClick={() => this._onTypeSelect(type)}>{this.i18n('statistics.type.' + type)}</Button>);
        });
        return buttons;
    }
}

export default injectIntl(I18nWrapper(Statistics));
