/**
 * Created by ledvima1 on 27.5.15.
 */
'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var ModalTrigger = require('react-bootstrap').ModalTrigger;
var Modal = require('react-bootstrap').Modal;
var OverlayMixin = require('react-bootstrap').OverlayMixin;

var Actions = require('../../actions/Actions');
var Utils = require('../../utils/Utils.js');

var DESCRIPTION_LENGTH_THRESHOLD = 100;

var ReportRow = React.createClass({
    mixins: [OverlayMixin],

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
    toggleModal: function () {
        this.setState({modalOpen: !this.state.modalOpen});
    },
    removeReport: function () {
        Actions.deleteReport(this.props.report);
        this.toggleModal();
    },
    render: function () {
        var report = this.props.report;
        var date = new Date(report.eventTime);
        var formattedDate = Utils.formatDate(date);
        var description = report.description.length > DESCRIPTION_LENGTH_THRESHOLD ?
            (report.description.substring(0, DESCRIPTION_LENGTH_THRESHOLD) + '...') : report.description;
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td>{report.name}</td>
                <td>{formattedDate}</td>
                <td title={report.description}>{description}</td>
                <td>
                    <span className="actions">
                        <Button bsStyle="primary" bsSize="small" onClick={this.onEditClick}>Edit</Button>
                    </span>
                    <span className="actions">
                    <Button bsStyle="danger" bsSize="small" onClick={this.onDeleteClick}>Delete</Button>
                    </span>
                </td>
            </tr>
        );
    },
    renderOverlay: function () {
        if (!this.state.modalOpen) {
            return <span/>
        }
        return (
            <Modal title='Delete Event Report?' onRequestHide={this.toggleModal}>
                <div className="modal-body">
                    Are you sure you want to remove this report?
                </div>
                <div className="modal-footer">
                    <Button bsStyle="primary" onClick={this.removeReport}>Delete</Button>
                    <Button onClick={this.toggleModal}>Cancel</Button>
                </div>
            </Modal>
        )
    }
});

module.exports = ReportRow;
