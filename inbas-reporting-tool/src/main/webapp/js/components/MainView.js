/**
 * Created by kidney on 7/7/15.
 */

'use strict';

var React = require('react');
var Router = require('react-router');
var RouteHandler = Router.RouteHandler;
var Reflux = require('reflux');

var Navbar = require('react-bootstrap').Navbar;
var Nav = require('react-bootstrap').Nav;
var DropdownButton = require('react-bootstrap').DropdownButton;
var MenuItem = require('react-bootstrap').MenuItem;
var NavItemLink = require('react-router-bootstrap').NavItemLink;

var Authentication = require('../utils/Authentication');
var UserStore = require('../stores/UserStore');

var MainView = React.createClass({
    mixins: [
        Reflux.listenTo(UserStore, 'onUserLoaded')
    ],

    getInitialState: function () {
        return {
            loggedIn: UserStore.isLoaded()
        }
    },

    onUserLoaded: function () {
        this.setState({loggedIn: true});
    },

    render: function () {
        if (!this.state.loggedIn) {
            return (<RouteHandler />);
        }
        var user = UserStore.getCurrentUser();
        var name = user.firstName.substr(0, 1) + '. ' + user.lastName;
        return (
            <div>
                <header>
                    <Navbar brand='INBAS Reporting Tool' fluid={true}>
                        <Nav>
                            <NavItemLink to='home'>Home</NavItemLink>
                            <NavItemLink to='reports'>Reports</NavItemLink>
                        </Nav>
                        <Nav right style={{margin: '0 -15px 0 0'}}>
                            <DropdownButton title={name}>
                                <MenuItem href='#' onClick={Authentication.logout}>Logout</MenuItem>
                            </DropdownButton>
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
