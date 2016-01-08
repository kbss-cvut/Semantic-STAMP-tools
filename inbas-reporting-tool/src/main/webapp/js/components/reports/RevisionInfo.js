'use strict';

var React = require('react');
var Input = require('react-bootstrap').Input;

var Logger = require('../../utils/Logger');
var Utils = require('../../utils/Utils');
var injectIntl = require('../../utils/injectIntl');
var I18nMixin = require('../../i18n/I18nMixin');

var RevisionInfo = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        revisions: React.PropTypes.array.isRequired,
        selectedRevision: React.PropTypes.number.isRequired,
        onSelect: React.PropTypes.func.isRequired
    },

    onSelect: function (e) {
        var revision = Number(e.target.value),
            revisions = this.props.revisions;
        for (var i = 0, len = revisions.length; i < len; i++) {
            if (revisions[i].revision === revision) {
                this.props.onSelect(revisions[i]);
                return;
            }
        }
        Logger.error('Revision ' + revision + ' not found!');
    },


    render: function () {
        return (
            <Input type='select' label={this.i18n('revisions.label')} value={this.props.selectedRevision}
                   onChange={this.onSelect}>
                {this.renderOptions()}
            </Input>
        )
    },

    renderOptions: function () {
        var revisions = this.props.revisions,
            options = [],
            formattedDate;
        for (var i = 0, len = revisions.length; i < len; i++) {
            formattedDate = Utils.formatDate(new Date(revisions[i].created));
            options.push(<option key={'rev_' + revisions[i].revision} value={revisions[i].revision}
                                 title={this.i18n('revisions.show-tooltip')}
                                 label={revisions[i].revision + ' - ' + this.i18n('revisions.created') + ': ' +
     formattedDate}/>);
        }
        return options;
    }
});

module.exports = injectIntl(RevisionInfo);
