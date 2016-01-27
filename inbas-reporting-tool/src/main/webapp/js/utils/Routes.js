'use strict';

var Routes = {

    login: {name: 'login', path: 'login'},
    register: {name: 'register', path: 'register'},
    dashboard: {name: 'dashboard', path: 'dashboard'},
    reports: {name: 'reports', path: 'reports'},
    createReport: {name: 'createReport', path: 'reports/create'},
    editReport: {name: 'editReport', path: 'reports/:reportKey'}
};

module.exports = Routes;
