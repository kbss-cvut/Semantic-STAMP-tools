'use strict';

var Routes = {

    login: {name: 'login', path: 'login'},
    register: {name: 'register', path: 'register'},
    dashboard: {name: 'dashboard', path: 'dashboard'},
    reports: {name: 'reports', path: 'reports'},
    preliminary: {name: 'preliminary', path: 'preliminary'},
    createReport: {name: 'createReport', path: 'preliminary/create'},
    editReport: {name: 'editReport', path: 'preliminary/:reportKey'},
    investigations: {name: 'investigations', path: 'investigations'},
    editInvestigation: {name: 'editInvestigation', path: 'investigations/:reportKey'}

};

module.exports = Routes;
