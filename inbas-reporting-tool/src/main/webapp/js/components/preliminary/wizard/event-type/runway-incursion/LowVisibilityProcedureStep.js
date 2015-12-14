/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');
var assign = require('object-assign');
var injectIntl = require('react-intl').injectIntl;

var Select = require('../../../../Select');
var Actions = require('../../../../../actions/Actions');
var Utils = require('../../../../../utils/Utils');
var OptionsStore = require('../../../../../stores/OptionsStore');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var LowVisibilityProcedureStep = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],

    getInitialState: function () {
        var statement = this.props.data.statement;
        return {
            statement: statement,
            options: []
        };
    },

    componentWillMount: function () {
        this.listenTo(OptionsStore, this.onLvpLoaded);
        Actions.loadLvpOptions();
    },

    onLvpLoaded: function (type, lvpOptions) {
        if (type !== 'lvp') {
            return;
        }
        var options = [];
        for (var i = 0, len = lvpOptions.length; i < len; i++) {
            options.push({
                value: lvpOptions[i],
                label: Utils.constantToString(lvpOptions[i], true)
            });
        }
        this.setState({options: options});
    },

    onLvpChange: function (e) {
        this.setState(assign(this.state.statement, {lvp: e.target.value}));
    },


    render: function () {
        return (
            <div className='form-group'>
                <Select label={this.i18n('eventtype.incursion.lvp.label')} onChange={this.onLvpChange}
                        value={this.state.statement.lvp} options={this.state.options}/>
            </div>
        );
    }
});

module.exports = injectIntl(LowVisibilityProcedureStep);
