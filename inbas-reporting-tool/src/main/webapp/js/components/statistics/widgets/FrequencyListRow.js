import React from "react";
import PropTypes from "prop-types";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";
import {Line, LineChart, Tooltip, XAxis} from "recharts";
import Utils from "../Utils";
import {Button} from "react-bootstrap";
import {Link} from "react-router";

class FrequencyListRow extends React.Component {

    static propTypes = {
        onClick: PropTypes.func,
        row: PropTypes.object.isRequired,
        active: PropTypes.bool
    };

    constructor(props) {
        super(props);
    };

    onMouseClick = () => {
        this.props.onClick(this.props.row.eventTypeIri);
    };

    onMouseClickCount = () => {
        return this.props.onClickCount(this.props.row.eventTypeIri);
    };

    render() {
        return <tr key={this.props.row.key}>
            <td>{!(this.props.onClick) ?
                <div>{this.props.row.eventType}</div> :
                <Button bsStyle="link" className={this.props.active ? "active-item" : undefined}
                        onClick={this.onMouseClick}
                        title={this.props.row.eventTypeIri}>{this.props.row.eventType}
                </Button>}
            </td>
            <td className="content-center vertical-middle">
                {!(this.props.onClickCount) ?
                this.props.row.totalSum :
                <Link className={this.props.active ? "active-item" : undefined}
                        to={this.onMouseClickCount()}
                        title={this.props.row.totalSum}>{this.props.row.totalSum}
                </Link>}
            </td>
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
