'use strict';

var React = require('react');
var Typeahead = require('react-bootstrap-typeahead');
var TypeaheadResultList = require('../../typeahead/TypeaheadResultList');
var Vocabulary = require('../../../constants/Vocabulary');

var FormTypeahead = React.createClass({

    propTypes: {
        options: React.PropTypes.object.isRequired,
        item: React.PropTypes.object.isRequired
    },

    render: function () {
        var item = this.props.item,
            options = this.props.options;
        return <Typeahead className='form-group form-group-sm' formInputOption='id' optionsButton={true}
                          placeholder={item[Vocabulary.RDFS_LABEL]} filterOption='name' displayOption='name'
                          options={options[item['@id']] ? options[item['@id']] : []}
                          customListComponent={TypeaheadResultList}/>;
    }
});

module.exports = FormTypeahead;
