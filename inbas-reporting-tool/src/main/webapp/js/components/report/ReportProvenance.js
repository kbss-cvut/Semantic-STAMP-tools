'use strict';

import React from "react";
import {FormattedMessage} from "react-intl";
import Utils from "../../utils/Utils";

export default class ReportProvenance extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        revisions: React.PropTypes.object
    };

    constructor(props) {
        super(props);
    }

    _renderProvenanceInfo() {
        var report = this.props.report;
        if (report.isNew) {
            return null;
        }
        var author = report.author ? report.author.firstName + ' ' + report.author.lastName : '',
            created = Utils.formatDate(new Date(report.dateCreated)),
            lastEditor, lastModified;
        if (!report.lastModified) {
            return <div className='notice-small'>
                <FormattedMessage id='report.created-by-msg'
                                  values={{date: created, name: <b>{author}</b>}}/>
            </div>;
        }
        lastEditor = report.lastModifiedBy ? report.lastModifiedBy.firstName + ' ' + report.lastModifiedBy.lastName : '';
        lastModified = Utils.formatDate(new Date(report.lastModified));
        return <div className='notice-small'>
            <FormattedMessage id='report.created-by-msg'
                              values={{date: created, name: <b>{author}</b>}}/>
            &nbsp;
            <FormattedMessage id='report.last-edited-msg'
                              values={{date: lastModified, name: <b>{lastEditor}</b>}}/>
        </div>;
    }

    render() {
        return <div>
            <div className='row'>
                <div className='col-xs-4'>
                    {this.props.revisions}
                </div>
            </div>
            {this._renderProvenanceInfo()}
        </div>;
    }
}
