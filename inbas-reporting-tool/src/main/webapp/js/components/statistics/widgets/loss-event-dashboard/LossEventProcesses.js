import React from "react";
import PropTypes from "prop-types";
import {injectIntl} from "react-intl";
import {Button, Table} from "react-bootstrap";
import I18nWrapper from "../../../../i18n/I18nWrapper";
import Utils from "../../Utils";

function stripProcessPrefix(label) {
    if (label.startsWith("process-")) {
        return label.substring(8).trim();
    }
    return label;
}

const LossEventProcesses = props => {
    const data = props.data;
    if (!data) {
        return null;
    }
    const rowData = Utils.sparql2table(data);

    return <div className="autoscroll" style={{maxHeight: "400px"}}>
        <h4>{props.i18n("statistics.panel.loss-events.processes.label")}</h4>
        <Table striped bordered condensed hover>
            <thead>
            <tr>
                <th className='col-xs-4 content-center'>{props.i18n('statistics.panel.loss-events.process')}</th>
                <th className='col-xs-1 content-center'>{props.i18n('count')}</th>
            </tr>
            </thead>
            <tbody>
            {rowData.map(row => <tr key={row.process_type_iri}>
                <td>
                    <Button bsStyle="link" style={{whiteSpace: "normal"}}
                            title={row.process_type_iri} onClick={() => props.onSelect({
                        id: row.process_type_iri,
                        label: row.controlled_process
                    })}>{stripProcessPrefix(row.controlled_process)}
                    </Button>
                </td>
                <td className="content-center vertical-middle">{row.count}</td>
            </tr>)}
            </tbody>
        </Table>
    </div>;
};

LossEventProcesses.propTypes = {
    data: PropTypes.array,
    onSelect: PropTypes.func.isRequired
};

export default injectIntl(I18nWrapper(LossEventProcesses));

