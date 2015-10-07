'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var Button = require('react-bootstrap').Button;

var Input = require('../Input');

var FactorDetail = React.createClass({
    propTypes: {
        onSave: React.PropTypes.func.isRequired,
        onClose: React.PropTypes.func.isRequired,
        onDelete: React.PropTypes.func.isRequired,
        factor: React.PropTypes.object
    },

    getInitialState: function () {
        return {
            showDeleteDialog: false
        };
    },

    onDeleteClick: function () {
        this.setState({showDeleteDialog: true});
    },

    onDeleteFactor: function () {
        this.setState({showDeleteDialog: false});
        this.props.onDelete();
    },

    onCancelDelete: function () {
        this.setState({showDeleteDialog: false});
    },


    render: function () {
        return (
            <Modal show={this.props.show} onHide={this.props.onClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Factor Detail</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this.renderDeleteDialog()}
                    <div className='form-group'>
                        <Input type='text' label='Event Type'/>
                    </div>
                    <div className='form-group'>
                        <label className='form-control'>Time period</label>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button bsSize='small' bsStyle='success' onClick={this.props.onSave}>Save</Button>
                    <Button bsSize='small' onClick={this.props.onClose}>Cancel</Button>
                    {this.renderDeleteButton()}
                </Modal.Footer>
            </Modal>
        )
    },

    renderDeleteButton: function () {
        return this.props.factor != null && this.props.factor.isNew ? null : (
            <Button bsSize='small' bsStyle='warning' onClick={this.onDeleteClick}>Delete</Button>);
    },

    renderDeleteDialog: function () {
        return (
            <Modal show={this.state.showDeleteDialog} onHide={this.onCancelDelete}>
                <Modal.Header>
                    <Modal.Title>Delete factor?</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to remove this factor?
                </Modal.Body>
                <Modal.Footer>
                    <Button bsSize='small' bsStyle='warning' onClick={this.onDeleteFactor}>Delete</Button>
                    <Button bsSize='small' onClick={this.onCancelDelete}>Cancel</Button>
                </Modal.Footer>
            </Modal>
        );
    }
});

module.exports = FactorDetail;
