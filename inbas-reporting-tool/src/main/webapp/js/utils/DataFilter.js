'use strict';

var Constants = require('../constants/Constants');

/**
 * Filters data according to specified filter(s).
 *
 * The filtering supports property paths - separated by dots, i.e. 'att.innerAtt' will filter the items by 'innerAtt'
 * of attribute 'att' of each item.
 * Filter value 'all' means to skip the given filter.
 */
var DataFilter = {

    filterData: function (data, filter) {
        if (this._canSkipFilter(filter)) {
            return data;
        }
        return data.filter(function (item) {
            for (var key in filter) {
                var i, len;
                if (!filter.hasOwnProperty(key) || filter[key] === Constants.FILTER_DEFAULT) {
                    continue;
                }
                var path = key.split('.'),
                    value = item;
                for (i = 0, len = path.length; i < len; i++) {
                    value = value[path[i]];
                }
                var filterValue = filter[key];
                if (!Array.isArray(filterValue)) {
                    return (Array.isArray(value) && value.indexOf(filterValue) !== -1) || (!Array.isArray(value) && value === filterValue);

                } else {
                    // If the filter itself is an array it suffices when a single value from the filter is present
                    for (i = 0, len = filterValue.length; i < len; i++) {
                        if (value === filterValue[i] || Array.isArray(value) && value.indexOf(filterValue[i]) !== -1) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        });
    },

    _canSkipFilter: function (filter) {
        if (!filter) {
            return true;
        }
        for (var key in filter) {
            if (filter[key] !== Constants.FILTER_DEFAULT) {
                return false;
            }
        }
        return true;
    }
};

module.exports = DataFilter;
