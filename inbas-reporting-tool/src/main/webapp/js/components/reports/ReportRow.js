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
var router = require('../../utils/router');

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
    onInvestigate: function () {
        router.transitionTo('investigation', {reportKey: this.props.report.key}, {onCancel: 'investigations'})
    },


    render: function () {
        var report = this.props.report;
        var date = new Date(report.occurrenceTime);
        var formattedDate = Utils.formatDate(date);
        // Have to set style directly, class style is overridden by the bootstrap styling
        var verticalAlign = {verticalAlign: 'middle'};
        return (
            <tr onDoubleClick={this.onDoubleClick}>
                <td style={verticalAlign}><a href='javascript:void(0);' onClick={this.onEditClick}
                                             title='Click to see report detail'>{report.name}</a></td>
                <td style={verticalAlign}>{formattedDate}</td>
                <td style={verticalAlign}><CollapsibleText text={report.initialReport}/></td>
                <td style={verticalAlign} className='actions'>
                    <Button bsStyle='primary' bsSize='small' title='Edit this occurrence report'
                            onClick={this.onEditClick}>Edit</Button>
                    <Button bsStyle='danger' bsSize='small' title='Delete this occurrence report'
                            onClick={this.onDeleteClick}>Delete</Button>
                    <Modal show={this.state.modalOpen} onHide={this.onCloseModal}>
                        <Modal.Header closeButton>
                            <Modal.Title>Delete Occurrence Report?</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            Are you sure you want to remove this report?
                        </Modal.Body>
                        <Modal.Footer>
                            <Button bsStyle='primary' bsSize='small' onClick={this.removeReport}>Delete</Button>
                            <Button bsSize='small' onClick={this.onCloseModal}>Cancel</Button>
                        </Modal.Footer>
                    </Modal>
                    <Button bsStyle='primary' bsSize='small' title='Start investigation based on this report'
                            onClick={this.onInvestigate}>Investigate</Button>
                </td>
            </tr>
        );
    }
});

module.exports = ReportRow;
