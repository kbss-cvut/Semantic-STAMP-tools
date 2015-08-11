/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var React = require('react');
var router = require('./utils/router');
var Actions = require('./actions/Actions');


//Actions.loadReports();
Actions.loadUser();

router.run(function (Handler) {
    React.render(<Handler />, document.getElementById('content'));
});
