/**
 Main entry point for the ReactJS frontend
 */

'use strict';

var React = require('react');
var Router = require('react-router');

var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItemLink = require('react-router-bootstrap').NavItemLink;

var Home = require('./components/Home');
var ReportsController = require('./components/reports/ReportsController');
var Actions = require('./actions/Actions');

var DefaultRoute = Router.DefaultRoute;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;

var MainView = React.createClass({
    render: function () {
        return (
            <div>
                <header>
                    <Navbar brand="INBAS Reporting Tool">
                        <Nav>
                            <NavItemLink to="home">Home</NavItemLink>
                            <NavItemLink to="reports">Reports</NavItemLink>
                        </Nav>
                    </Navbar>
                </header>
                <section>
                    <RouteHandler />
                </section>
            </div>
        );
    }
});

var routes = (
    <Route handler={MainView} path="/">
        <Route name="home" path="home" handler={Home}/>
        <Route name="reports" path="reports" handler={ReportsController}/>
        <DefaultRoute handler={Home}/>
    </Route>
);

Router.run(routes, function (Handler) {
    React.render(<Handler />, document.getElementById('content'));
});
