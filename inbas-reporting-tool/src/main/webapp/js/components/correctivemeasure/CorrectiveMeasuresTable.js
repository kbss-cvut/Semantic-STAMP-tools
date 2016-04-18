'use strict';

import React from "react";
import {Table} from "react-bootstrap";
import injectIntl from "../../utils/injectIntl";
import I18nWrapper from "../../i18n/I18nWrapper";

var Row = (props) => {
    return <tr>
        <td key='cell_description' style={{whiteSpace: 'pre-wrap'}}>{props.data}</td>
        <td key='cell_actions' style={{verticalAlign: 'middle'}} className='actions'>
            <Button onClick={this.onEdit} title={props.i18n('preliminary.detail.table-edit-tooltip')}
                    bsSize='small' bsStyle='primary'>
                {props.i18n('table-edit')}
            </Button>
            <Button onClick={this.onRemove} title={props.i18n('preliminary.detail.table-delete-tooltip')}
                    bsSize='small' bsStyle='warning'>
                {props.i18n('delete')}
            </Button>
        </td>
    </tr>
};

Row = injectIntl(I18nWrapper(Row));


class CorrectiveMeasuresTable extends React.Component {
    static propTypes = {
        data: React.PropTypes.array.isRequired,
        handlers: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        var data = this.props.data,
            handlers = this.props.handlers,
            rows = [];
        for (var i = 0, len = data.length; i < len; i++) {
            rows.push(<Row key={'corrective' + i} statementIndex={i} data={data[i].description}
                           onRemove={handlers.onRemove} onEdit={handlers.onEdit}/>);
        }
        return (
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th key='header_desc'
                        className='col-xs-11'>{this.props.i18n('preliminary.detail.corrective.table-description')}</th>
                    <th key='header_actions' className='col-xs-1'>{this.props.i18n('table-actions')}</th>
                </tr>
                </thead>
                <tbody>
                {rows}
                </tbody>
            </Table>
        );
    }
}

export default injectIntl(I18nWrapper(CorrectiveMeasuresTable));
