/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var Reflux = require('reflux');

var Actions = Reflux.createActions(['loadUser', 'loadReports', 'createReport', 'updateReport', 'deleteReport']);

module.exports = Actions;
