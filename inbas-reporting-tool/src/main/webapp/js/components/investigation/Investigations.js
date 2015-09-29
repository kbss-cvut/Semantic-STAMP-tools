/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var Investigations = React.createClass({
    render: function () {
        var component = this.props.investigations.length === 0 ? (
            <div className='italic' {...this.props}>There are currently no investigations.</div>) :
            this.renderInvestigations();
        var title = <h3>Occurrence Investigations</h3>;
        return (
            <Panel header={title} {...this.props}>
                {component}
            </Panel>
        );
    },

    renderInvestigations: function () {
        return (
            <Table striped bordered hover {...this.props}>
                <thead>
                <tr>
                    <th className='col-xs-2'>Occurrence date</th>
                    <th className='col-xs-8'>Description</th>
                    <th className='col-xs-2'>Actions</th>
                </tr>
                </thead>
            </Table>
        );
    }
});

module.exports = Investigations;
