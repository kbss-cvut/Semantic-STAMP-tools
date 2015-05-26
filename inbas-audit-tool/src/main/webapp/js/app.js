/**
 Main entry point for the ReactJS frontend
 */

var React = require('react');
var Router = require('react-router');

var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItemLink = require('react-router-bootstrap').NavItemLink;

var HelloWorld = require('./components/HelloWorld');
var ReportsController = require('./components/ReportsController');

var DefaultRoute = Router.DefaultRoute;
var Route = Router.Route;
var RouteHandler = Router.RouteHandler;

var MainView = React.createClass({
    render: function () {
        return (
            <div>
                <header>
                    <Navbar brand="INBAS Audit Tool">
                        <Nav>
                            <NavItemLink to="helloworld">Hello World!</NavItemLink>
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
        <Route name="helloworld" path="helloworld" handler={HelloWorld}/>
        <Route name="reports" path="reports" handler={ReportsController}/>
        <DefaultRoute handler={HelloWorld}/>
    </Route>
);

Router.run(routes, function (Handler) {
    React.render(<Handler />, document.getElementById('content'));
});
