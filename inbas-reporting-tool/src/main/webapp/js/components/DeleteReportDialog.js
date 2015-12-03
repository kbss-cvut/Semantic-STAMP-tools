/**
 * @jsx
 */
'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Button = require('react-bootstrap').Button;

var DeleteReportDialog = React.createClass({

    propTypes: {
        onClose: React.PropTypes.func.isRequired,
        onSubmit: React.PropTypes.func.isRequired,
        show: React.PropTypes.bool.isRequired,
        reportType: React.PropTypes.string
    },

    render: function () {
        var title = this.props.reportType ? 'Delete ' + this.props.reportType + ' Report?' : 'Delete Report?';
        return (
            <Modal show={this.props.show} onHide={this.props.onClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to remove this report?
                </Modal.Body>
                <Modal.Footer>
                    <Button bsStyle='warning' bsSize='small' onClick={this.props.onSubmit}>Delete</Button>
                    <Button bsSize='small' onClick={this.props.onClose}>Cancel</Button>
                </Modal.Footer>
            </Modal>
        );
    }
});

module.exports = DeleteReportDialog;
