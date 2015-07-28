/**
 * Created by ledvima1 on 11.6.15.
 */

var Utils = {
    /**
     * Formats the specified date into DD-MM-YY hh:mm A
     * @param date The date to format
     */
    formatDate: function(date) {
        var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate().toString();
        var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1).toString();
        var year = (date.getFullYear() % 100).toString();
        var h = date.getHours() % 12;
        var hour = h < 10 ? '0' + h : h.toString();
        var period = date.getHours() >= 12 ? 'PM' : 'AM';
        var minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes().toString();
        return (day + '-' + month + '-' + year + ' ' + hour + ':' + minute + ' ' + period);
    },

    /**
     * Returns a Java constant (uppercase with underscores) as a nicer string.
     *
     * Replaces underscores with spaces.
     */
    constantToString: function(constant) {
        return constant.replace(/_/g, ' ');
        //return words.charAt(0) + words.substring(1).toLowerCase();
    }
};

module.exports = Utils;