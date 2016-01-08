/**
 * @author ledvima1
 */
'use strict';

/**
 * Common propositions that should not be capitalized
 */
var PROPOSITIONS = [
    'a', 'about', 'across', 'after', 'along', 'among', 'an', 'around', 'as', 'aside', 'at', 'before', 'behind', 'below',
    'beneath', 'beside', 'besides', 'between', 'beyond', 'but', 'by', 'for', 'given', 'in', 'inside', 'into', 'like', 'near',
    'of', 'off', 'on', 'onto', 'outside', 'over', 'since', 'than', 'through', 'to', 'until', 'up', 'via', 'with', 'within',
    'without'
];
var WORD_LENGTH_THRESHOLD = 4;

var Utils = {
    /**
     * Formats the specified date into DD-MM-YY HH:mm
     * @param date The date to format
     */
    formatDate: function (date) {
        var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate().toString();
        var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1).toString();
        var year = (date.getFullYear() % 100).toString();
        var h = date.getHours();
        var hour = h < 10 ? '0' + h : h.toString();
        var minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes().toString();
        return (day + '-' + month + '-' + year + ' ' + hour + ':' + minute);
    },

    /**
     * Returns a Java constant (uppercase with underscores) as a nicer string.
     *
     * Replaces underscores with spaces. And if capitalize is selected, capitalizes the words.
     */
    constantToString: function (constant, capitalize) {
        if (!capitalize) {
            return constant.replace(/_/g, ' ');
        }
        var words = constant.split('_');
        for (var i = 0, len = words.length; i < len; i++) {
            var word = words[i];
            if (word.length < WORD_LENGTH_THRESHOLD) {
                if (PROPOSITIONS.indexOf(word) === -1) {
                    continue;
                }
                words[i] = word.toLowerCase();
            }
            words[i] = word.charAt(0) + word.substring(1).toLowerCase();
        }
        return words.join(' ');
    },

    /**
     * Converts the specified time value from one time unit to the other.
     *
     * Currently supported units are seconds, minutes and hours. When converting to larger units (e.g. from seconds to
     * minutes), the result is rounded to integer.
     *
     * @param fromUnit Unit to convert from
     * @param toUnit Target unit
     * @param value The value to convert
     * @return {*} Converted value
     */
    convertTime: function (fromUnit, toUnit, value) {
        if (fromUnit === toUnit) {
            return value;
        }
        switch (fromUnit) {
            case 'second':
                if (toUnit === 'minute') {
                    return Math.round(value / 60);
                } else {
                    return Math.round(value / 60 / 60);
                }
            case 'minute':
                if (toUnit === 'second') {
                    return 60 * value;
                } else {
                    return Math.round(value / 60);
                }
            case 'hour':
                if (toUnit === 'second') {
                    return 60 * 60 * value;
                } else {
                    return 60 * value;
                }
            default:
                return value;
        }
    },

    /**
     * Extracts report key from location header in the specified Ajax response.
     * @param response Ajax response
     * @return {string} Report key as string
     */
    extractKeyFromLocationHeader: function (response) {
        var location = response.headers['location'];
        if (!location) {
            return '';
        }
        return location.substring(location.lastIndexOf('/') + 1);
    }
};

module.exports = Utils;