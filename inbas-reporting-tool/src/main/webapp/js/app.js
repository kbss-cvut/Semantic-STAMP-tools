/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var React = require('react');
var router = require('./utils/router');
var Actions = require('./actions/Actions');

var UserStore = require('./stores/UserStore');
var Login = require('./components/login/Login');


//Actions.loadReports();
Actions.loadUser();

router.run(function (Handler) {
    React.render(<Handler />, document.getElementById('content'));
});
