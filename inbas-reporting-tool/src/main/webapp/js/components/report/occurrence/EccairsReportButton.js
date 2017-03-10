'use strict';

import React from "react";
import {Button} from "react-bootstrap";
import Actions from "../../../actions/Actions";
import Constants from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import ReportStore from "../../../stores/ReportStore";
import Routes from "../../../utils/Routes";
import Routing from "../../../utils/Routing";

class EccairsReportButton extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            loading: false
        };
    }

    componentDidMount() {
        this.unsubscribe = ReportStore.listen(this._onLoadFinished);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onClick = () => {
        this.setState({loading: true});
        Actions.loadEccairsReport(this.props.report);
    };

    _onLoadFinished = (data) => {
        if (data.action !== Actions.loadEccairsReport) {
            return;
        }
        this.setState({loading: false});
        if (data.key) {
            Routing.transitionTo(Routes.editReport, {params: {reportKey: data.key}});
        } else {
            Actions.publishMessage('report.eccairs.error-msg', Constants.MESSAGE_TYPE.ERROR, Actions.loadEccairsReport);
        }
    };

    render() {
        let report = this.props.report,
            loading = this.state.loading;
        if (!report.isEccairsReport()) {
            return false;
        }
        return <Button className='detail-top-button' bsSize='small' bsStyle='primary' onClick={this._onClick}
                       disabled={loading} title={this.i18n('report.eccairs.button.tooltip')}>
            {this.i18n(loading ? 'please-wait' : 'report.eccairs.button.label')}
        </Button>;
    }
}

export default injectIntl(I18nWrapper(EccairsReportButton));
