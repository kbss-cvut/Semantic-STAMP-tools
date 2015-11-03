/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var React = require('react');
var ReactDOM = require('react-dom');
var Router = require('react-router').Router;

var history = require('./utils/Routing').history;
var Routes = require('./utils/Routes');
var Actions = require('./actions/Actions');

Actions.loadUser();

ReactDOM.render((
    <Router history={history} routes={Routes}/>
), document.getElementById('content'));
