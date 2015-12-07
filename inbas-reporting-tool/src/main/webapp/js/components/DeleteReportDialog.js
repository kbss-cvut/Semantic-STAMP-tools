/**
 * @jsx
 */
'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Button = require('react-bootstrap').Button;

var IntlMixin = require('react-intl').IntlMixin;

var DeleteReportDialog = React.createClass({
    mixins: [IntlMixin],

    propTypes: {
        onClose: React.PropTypes.func.isRequired,
        onSubmit: React.PropTypes.func.isRequired,
        show: React.PropTypes.bool.isRequired,
        reportType: React.PropTypes.string
    },

    render: function () {
        // TODO Use formatted message when Intl is fixed
        var title = this.props.reportType ? 'Delete ' + this.props.reportType + ' Report?' : 'Delete Report?';
        return (
            <Modal show={this.props.show} onHide={this.props.onClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this.getIntlMessage('delete-dialog.content')}
                </Modal.Body>
                <Modal.Footer>
                    <Button bsStyle='warning' bsSize='small'
                            onClick={this.props.onSubmit}>{this.getIntlMessage('delete')}</Button>
                    <Button bsSize='small' onClick={this.props.onClose}>{this.getIntlMessage('cancel')}</Button>
                </Modal.Footer>
            </Modal>
        );
    }
});

module.exports = DeleteReportDialog;
