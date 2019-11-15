'use strict';

import React from "react";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import {Line, LineChart, Tooltip, XAxis} from "recharts";
import Utils from "../Utils";
import {Button} from "react-bootstrap";

class FrequencyListRow extends React.Component {

    static propTypes = {
        // onClick: React.PropTypes.function,
        row: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
    };

    onMouseClick = () => {
        this.props.onClick(this.props.row.eventTypeIri);
    };

    render() {
        return <tr key={this.props.row.key}>
            <td>{!(this.props.onClick) ?
                <div>{this.props.row.eventType}</div> :
                <Button bsStyle="link"
                        onClick={this.onMouseClick}
                        title={this.props.row.eventTypeIri}>{this.props.row.eventType}
                </Button>}
            </td>
            <td className="content-center vertical-middle">{this.props.row.totalSum}</td>
            <td className="content-center vertical-middle"><LineChart width={100} height={30} data={this.props.row.data}>
                <XAxis dataKey='date' hide={true} tickFormatter={Utils.getDateString}/>
                <Line type='basis' dataKey='count' stroke='#8884d8' strokeWidth={2} dot={false}/>
                <Tooltip labelFormatter={Utils.getDateString}/>
            </LineChart>
            </td>
        </tr>
    };
}

export default injectIntl(I18nWrapper(FrequencyListRow));
