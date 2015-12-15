/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var injectIntl = require('react-intl').injectIntl;

var Input = require('../../../../Input');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var PersonIntruder = React.createClass({
    mixins: [I18nMixin],

    render: function () {
        var statement = this.props.statement;
        return (
            <Panel header={this.i18n('eventtype.incursion.intruder.person.panel-title')}
                   style={{margin: '15px 0px 0px 0px'}}>
                <div className='row'>
                    <div className='col-xs-6'>
                        <Input type='text' label={this.i18n('eventtype.incursion.intruder.person.category')}
                               name='category'
                               value={statement.intruder.category}
                               onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.person.category-tooltip')}/>
                    </div>
                    <div className='col-xs-6'>
                        <Input type='text' label={this.i18n('eventtype.incursion.intruder.organization')}
                               name='organization' value={statement.intruder.organization}
                               onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.person.organization-tooltip')}/>
                    </div>
                </div>
                <div className='row'>
                    <div className='col-xs-12'>
                        <Input type='textarea' label={this.i18n('eventtype.incursion.intruder.person.wasdoing')}
                               name='wasDoing'
                               value={statement.intruder.wasDoing} onChange={this.props.onChange}
                               title={this.i18n('eventtype.incursion.intruder.person.wasdoing')}/>
                    </div>
                </div>
            </Panel>
        );
    }
});

module.exports = injectIntl(PersonIntruder);
