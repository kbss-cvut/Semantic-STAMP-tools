/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;

var Utils = require('../../utils/Utils.js');
var CollapsibleText = require('../CollapsibleText');
var DeleteReportDialog = require('../DeleteReportDialog');

var ReportRow = React.createClass({

    getInitialState: function () {
        return {
            modalOpen: false
        };
    },
    onDoubleClick: function (e) {
        e.preventDefault();
        this.onEditClick();
    },
    onEditClick: function () {
        this.props.actions.onEdit(this.props.report);
    },
    onDeleteClick: function () {
        this.setState({modalOpen: true});
    },
    onCloseModal: function () {
        this.setState({modalOpen: false});
    },
    removeReport: function () {
        this.props.actions.onRemove(this.props.report);
        this.onCloseModal();
    },
    onInvestigate: function () {
        this.props.actions.onInvestigate(this.props.report);
    },


    render: function () {
        var report = this.props.report;
        var date = new Date(report.occurrence.startTime);
        var formattedDate = Utils.formatDate(date);
        // Have to set style directly, class style is overridden by the bootstrap styling
        var verticalAlign = {verticalAlign: 'middle'};
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td style={verticalAlign}><a href='javascript:void(0);' onClick={this.onEditClick}
                                             title='Click to see report detail'>{report.occurrence.name}</a></td>
                <td style={verticalAlign}>{formattedDate}</td>
                <td style={verticalAlign}><CollapsibleText text={report.summary}/></td>
                <td style={verticalAlign} className='actions'>
                    <Button bsStyle='primary' bsSize='small' title='Edit this occurrence report'
                            onClick={this.onEditClick}>Edit</Button>
                    <Button bsStyle='warning' bsSize='small' title='Delete this occurrence report'
                            onClick={this.onDeleteClick}>Delete</Button>
                    <Button bsStyle='primary' bsSize='small' title='Investigate this occurrence'
                            onClick={this.onInvestigate}>Investigate
                    </Button>

                    <DeleteReportDialog show={this.state.modalOpen} onClose={this.onCloseModal}
                                        onSubmit={this.removeReport} reportType={'Preliminary'}/>
                </td>
            </tr>
        );
    }
});

module.exports = ReportRow;
