/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Loader = require('halogen/ClipLoader');

var Mask = React.createClass({

    propTypes: {
        text: React.PropTypes.string
    },

    render: function () {
        var text = this.props.text ? this.props.text : 'Please Wait...';
        return (
            <div className='mask'>
                <div className='spinner-container'>
                    <div style={{width: 32, height: 32, margin: 'auto'}}>
                        <Loader color='#337ab7' size='32px'/>
                    </div>
                    <div className='spinner-message'>{text}</div>
                </div>
            </div>
        );
    }
});

module.exports = Mask;
