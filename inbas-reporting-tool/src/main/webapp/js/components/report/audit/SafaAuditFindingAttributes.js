'use strict';

import React from "react";
import {FormattedMessage} from "react-intl";
import JsonLdUtils from "jsonld-utils";
import Actions from "../../../actions/Actions";
import Constans from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import Input from "../../Input";
import OptionsStore from "../../../stores/OptionsStore";
import Utils from "../../../utils/Utils";
import Vocabulary from "../../../constants/Vocabulary";

class SafaAuditFindingAttributes extends React.Component {
    static propTypes = {
        finding: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
    }

    componentDidMount() {
        Actions.loadOptions(Constans.OPTIONS.AUDIT_FINDING_STATUS);
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type) => {
        if (type === Constans.OPTIONS.AUDIT_FINDING_STATUS) {
            this.forceUpdate();
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    render() {
        let finding = this.props.finding,
            formattedDate = finding.statusLastModified ? Utils.formatDate(new Date(this.props.finding.statusLastModified)) : '';
        return <div>
            <div className='row'>
                <div className='col-xs-4'>
                    <Input value={finding.level} label={this.i18n('audit.finding.level.label')} readOnly/>
                </div>
            </div>
            <div className='row'>
                <div className='col-xs-4'>
                    {this._renderStatus()}
                    <div className='notice-small form-group'>
                        {formattedDate &&
                        <FormattedMessage id='audit.finding.status.lastModified' values={{date: formattedDate}}/>}
                    </div>
                </div>
            </div>
        </div>;
    }

    _renderStatus() {
        let status = this.props.finding.status,
            options = OptionsStore.getOptions(Constans.OPTIONS.AUDIT_FINDING_STATUS),
            option = options.find((item) => item['@id'] === status);
        let value = option ? JsonLdUtils.getLocalized(option[Vocabulary.RDFS_LABEL], this.props.intl) : '';
        return <Input value={value} label={this.i18n('audit.finding.status.label')}
                      title={this.i18n('audit.finding.status.tooltip')} readOnly/>;
    }
}

export default injectIntl(I18nWrapper(SafaAuditFindingAttributes));
