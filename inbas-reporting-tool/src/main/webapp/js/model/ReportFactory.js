'use strict';

module.exports = {
    createReport: function () {
        return {
            // Round the time to whole seconds
            occurrenceStart: (Date.now() / 1000) * 1000,
            occurrenceEnd: (Date.now() / 1000) * 1000,
            occurrence: {
                name: ''
            },
            isNew: true
        };
    }
};
