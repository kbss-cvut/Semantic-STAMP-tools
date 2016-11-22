'use strict';

import React from "react";
import {Button} from "react-bootstrap";
import Actions from "../../../actions/Actions";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";

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

    _onClick = () => {
        this.setState({loading: true});
        Actions.loadEccairsReport(this.props.report);
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
