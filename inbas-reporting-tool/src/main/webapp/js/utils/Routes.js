'use strict';

module.exports = {

    login: {name: 'login', path: 'login'},
    register: {name: 'register', path: 'register'},
    dashboard: {name: 'dashboard', path: 'dashboard'},
    reports: {name: 'reports', path: 'reports'},
    reportsWithLossEventType: {name: 'reportsWithLossEventType', path: 'reports?lossEventType=:lossEventType'},
    reportsWithEventType: {name: 'reportsWithEventType', path: 'reports?eventType=:eventType'},
    statistics: {name: 'statistics', path: 'statistics'},
    createReport: {name: 'createReport', path: 'reports/create'},
    editReport: {name: 'editReport', path: 'reports/:reportKey'},
    searchResults: {name: 'searchResults', path: 'search'},
    administration: {name: 'admin', path: 'admin'}
};
