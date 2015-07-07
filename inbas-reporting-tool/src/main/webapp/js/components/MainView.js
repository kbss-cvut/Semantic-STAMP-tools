/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var React = require('react');
var Router = require('react-router');
var RouteHandler = Router.RouteHandler;

var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var NavItemLink = require('react-router-bootstrap').NavItemLink;

var MainView = React.createClass({
    render: function () {
        return (
            <div>
                <header>
                    <Navbar brand='INBAS Reporting Tool'>
                        <Nav>
                            <NavItemLink to='home'>Home</NavItemLink>
                            <NavItemLink to='reports'>Reports</NavItemLink>
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

module.exports = MainView;
