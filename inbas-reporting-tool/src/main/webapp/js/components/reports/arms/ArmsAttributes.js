'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Input = require('../../Input');

var OptionsStore = require('../../../stores/OptionsStore');

var injectIntl = require('../../../utils/injectIntl');
var I18nMixin = require('../../../i18n/I18nMixin');

var ArmsAttributes = React.createClass({
   mixins: [I18nMixin],

    propTypes: {
        onAttributeChange: React.PropTypes.func.isRequired,
        report: React.PropTypes.object.isRequired
    }

});
