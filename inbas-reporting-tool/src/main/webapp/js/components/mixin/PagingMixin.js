var React = require('react');
var Pagination = require('react-bootstrap').Pagination;

var MAX_BUTTONS = 5;

/**
 * Mixin for data paging in tables.
 *
 * It expects a state
 */
var PagingMixin = {

    propTypes: {
        pageSize: React.PropTypes.number,
        maxButtons: React.PropTypes.number
    },

    getDefaultProps: function () {
        return {
            pageSize: 10,
            maxButtons: MAX_BUTTONS
        }
    },

    getInitialState: function () {
        return {activePage: 1};
    },

    _onPageSelect: function (e, selectedEvent) {
        this.setState({activePage: selectedEvent.eventKey});
    },

    /**
     * Resets pagination to defaults.
     */
    resetPagination: function() {
        this.setState(this.getInitialState());
    },

    /**
     * Assuming that the data is an array, this function returns a sub-array of the data corresponding to the currently
     * active page.
     * @param data array of data
     */
    getCurrentPage: function (data) {
        var startIndex = this.props.pageSize * (this.state.activePage - 1),
            endIndex = startIndex + this.props.pageSize > data.length ? data.length : startIndex + this.props.pageSize;
        return data.slice(startIndex, endIndex);
    },

    renderPagination: function (data) {
        var itemCount = Math.ceil(data.length / this.props.pageSize);
        if (itemCount === 1) {
            return null;
        }
        return (<Pagination
            prev next first last ellipsis boundaryLinks items={itemCount} maxButtons={this.props.maxButtons}
            activePage={this.state.activePage} onSelect={this._onPageSelect}/>);
    }
};

module.exports = PagingMixin;
