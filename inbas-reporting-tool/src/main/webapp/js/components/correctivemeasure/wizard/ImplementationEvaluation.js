'use strict';

import React from "react";
import {Panel} from "react-bootstrap";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import Select from "../../Select";

class ImplementationEvaluation extends React.Component {

    static propTypes = {
        measure: React.PropTypes.object.isRequired
    };

    render() {
        var i18n = this.props.i18n,
            evaluation = this.props.measure.evalution;
        return <Panel header={<h5>{i18n('report.corrective.panel-title')}</h5>} bsStyle='info'>
            <Select options={[]} label={i18n('report.corrective.evaluation.evaluation')}/>
            <Input type='textarea' rows='8' label={i18n('report.corrective.evaluation.evaluation-notes')}
                   name='description' placeholder={i18n('report.corrective.evaluation.evaluation-notes')}
                   value={evaluation.description} onChange={this.props.onChange}/>
        </Panel>;
    }
}

export default injectIntl(I18nWrapper(ImplementationEvaluation));
