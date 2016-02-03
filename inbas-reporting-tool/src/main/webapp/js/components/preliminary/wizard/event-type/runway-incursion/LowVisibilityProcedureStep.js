/**
 * @jsx
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var injectIntl = require('../../../../../utils/injectIntl');

var Select = require('../../../../Select');
var Utils = require('../../../../../utils/Utils');
var OptionsStore = require('../../../../../stores/OptionsStore');
var I18nMixin = require('../../../../../i18n/I18nMixin');

var LowVisibilityProcedureStep = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        var statement = this.props.data.statement;
        return {
            statement: statement
        };
    },

    onLvpChange: function (e) {
        this.setState(assign(this.state.statement, {lvp: e.target.value}));
    },

    getLvpOptions: function () {
        var options = [],
            lvpOptions = OptionsStore.getLowVisibilityProcedureOptions();
        for (var i = 0, len = lvpOptions.length; i < len; i++) {
            options.push({
                value: lvpOptions[i],
                label: Utils.constantToString(lvpOptions[i], true)
            });
        }
        return options;
    },


    render: function () {
        return (
            <div className='form-group'>
                <Select label={this.i18n('eventtype.incursion.lvp.label')} onChange={this.onLvpChange}
                        value={this.state.statement.lvp} options={this.getLvpOptions()}/>
            </div>
        );
    }
});

module.exports = injectIntl(LowVisibilityProcedureStep);
