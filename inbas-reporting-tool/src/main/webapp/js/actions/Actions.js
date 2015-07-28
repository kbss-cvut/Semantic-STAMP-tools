/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'loadUser',
    'loadReports', 'findReport', 'createReport', 'updateReport', 'deleteReport',
    'loadEventTypes', 'loadLvpOptions'
]);

module.exports = Actions;
