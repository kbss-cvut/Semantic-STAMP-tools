/**
 * Created by ledvima1 on 27.5.15.
 */
'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Modal = require('react-bootstrap').Modal;

var Actions = require('../../actions/Actions');
var Utils = require('../../utils/Utils.js');
var CollapsibleText = require('../CollapsibleText');

var DESCRIPTION_LENGTH_THRESHOLD = 100;

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
        this.props.onEditReport(this.props.report);
    },
    onDeleteClick: function () {
        this.setState({modalOpen: true});
    },
    onCloseModal: function () {
        this.setState({modalOpen: false});
    },
    removeReport: function () {
        Actions.deleteReport(this.props.report);
        this.onCloseModal();
    },
    render: function () {
        var report = this.props.report;
        var date = new Date(report.eventTime);
        var formattedDate = Utils.formatDate(date);
        // Have to set style directly, class style is overridden by the bootstrap styling
        var verticalAlign = {verticalAlign: 'middle'};
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td style={verticalAlign}>{report.name}</td>
                <td style={verticalAlign}>{formattedDate}</td>
                <td><CollapsibleText text={report.description}/></td>
                <td style={verticalAlign}>
                    <span className="actions">
                        <Button bsStyle="primary" bsSize="small" onClick={this.onEditClick}>Edit</Button>
                    </span>
                    <span className="actions">
                        <Button bsStyle="danger" bsSize="small" onClick={this.onDeleteClick}>Delete</Button>
                        <Modal show={this.state.modalOpen} onHide={this.onCloseModal}>
                            <Modal.Header closeButton>
                                <Modal.Title>Delete Event Report?</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                Are you sure you want to remove this report?
                            </Modal.Body>
                            <Modal.Footer>
                                <Button bsStyle="primary" onClick={this.removeReport}>Delete</Button>
                                <Button onClick={this.onCloseModal}>Cancel</Button>
                            </Modal.Footer>
                        </Modal>
                    </span>
                </td>
            </tr>
        );
    }
});

module.exports = ReportRow;
