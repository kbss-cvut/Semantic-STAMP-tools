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

var UserStore = require('../stores/UserStore');

var MainView = React.createClass({
    getInitialState: function() {
        return {
            loggedIn: UserStore.isLoaded()
        }
    },

    render: function () {
        return (
            <div>
                <header>
                    <Navbar brand='INBAS Reporting Tool'>
                        <Nav>
                            <NavItemLink to='home' disabled={!this.state.loggedIn}>Home</NavItemLink>
                            <NavItemLink to='reports' disabled={!this.state.loggedIn}>Reports</NavItemLink>
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
