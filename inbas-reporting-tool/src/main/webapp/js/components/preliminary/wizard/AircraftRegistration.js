/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Panel = require('react-bootstrap').Panel;
var Fade = require('react-bootstrap').Fade;

var injectIntl = require('react-intl').injectIntl;

var Input = require('../../Input');
var EccairsLink = require('../../EccairsLink');
var I18nMixin = require('../../../i18n/I18nMixin');

var AircraftRegistration = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        return {
            expanded: false
        };
    },

    onToggleCollapsible: function (e) {
        e.preventDefault();
        this.setState({expanded: !this.state.expanded});
    },


    render: function () {
        var registrationLabel = (<span>{this.i18n('aircraft.registration')} (<EccairsLink text='244'/>)</span>);
        var stateLabel = (<span>{this.i18n('aircraft.state-of-registry')} (<EccairsLink text='281'/>)</span>);
        return (
            <div>
                <div className='form-group'>
                    <Button bsStyle='link' onClick={this.onToggleCollapsible}>{this.i18n('aircraft.registration')}</Button>
                </div>
                <Fade in={this.state.expanded} unmountOnExit={true} timeout={100}>
                    <Panel ref='panel'>
                        <div className='row'>
                            <div className='col-xs-6'>
                                <Input type='text' label={registrationLabel} name='registration' tabIndex='1'
                                       value={this.props.registration} onChange={this.props.onChange}
                                       title={this.i18n('aircraft.registration')}/>
                            </div>
                            <div className='col-xs-6'>
                                <Input type='text' label={stateLabel} name='stateOfRegistry' tabIndex='2'
                                       value={this.props.stateOfRegistry} onChange={this.props.onChange}
                                       title={this.i18n('aircraft.state-of-registry')}/>
                            </div>
                        </div>
                    </Panel>
                </Fade>
            </div>
        );
    }
});

module.exports = injectIntl(AircraftRegistration);
