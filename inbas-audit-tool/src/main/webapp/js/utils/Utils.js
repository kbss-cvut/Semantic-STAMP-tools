/**
 * Created by ledvima1 on 11.6.15.
 */

var Utils = {
    /**
     * Formats the specified date into DD-MM-YY hh:mm
     * @param date The date to format
     */
    formatDate: function(date) {
        var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate().toString();
        var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1).toString();
        var year = (date.getFullYear() % 100).toString();
        var hour = date.getHours() < 10 ? '0' + date.getHours() : date.getHours().toString();
        var minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes().toString();
        return (day + '-' + month + '-' + year + ' ' + hour + ':' + minute);
    }
};

module.exports = Utils;