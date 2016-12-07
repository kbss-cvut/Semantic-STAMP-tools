'use strict';

import React from "react";
import {Button, Panel} from "react-bootstrap";
import Actions from "../../actions/Actions";
import Constants from "../../constants/Constants";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import OptionsStore from "../../stores/OptionsStore";

class Statistics extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            type: 'general'
        }
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onConfigLoaded);
        Actions.loadStatisticsConfig();
    }

    _onConfigLoaded = (data) => {
        if (data.action === Actions.loadStatisticsConfig) {
            this.forceUpdate();
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onTypeSelect = (type) => {
        this.setState({type: type});
    };

    render() {
        const src = OptionsStore.getStatisticsConfiguration()[Constants.STATISTICS[this.state.type]];
        return <Panel header={<h3>{this.i18n('main.statistics-nav')}</h3>} bsStyle='primary'>
            <div className='statistics-switchers content-center'>
                {this._renderStatisticsTypeButtons()}
            </div>
            <iframe className='statistics-frame' src={src} allowFullScreen='true'></iframe>
        </Panel>;
    }

    _renderStatisticsTypeButtons() {
        const buttons = [];
        Object.getOwnPropertyNames(Constants.STATISTICS).forEach(type => {
            buttons.push(<Button key={type} bsStyle={this.state.type === type ? 'primary' : 'default'}
                                 onClick={() => this._onTypeSelect(type)}>{this.i18n('statistics.type.' + type)}</Button>);
        });
        return buttons;
    }
}

export default injectIntl(I18nWrapper(Statistics));
