'use strict';

import React from "react";
import {MenuItem, Nav, Navbar, NavDropdown, NavItem} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {IfGranted} from "react-authorization";
import classNames from "classnames";

import Actions from "../actions/Actions";
import Authentication from "../utils/Authentication";
import Constants from "../constants/Constants";
import I18nStore from "../stores/I18nStore";
import I18nWrapper from "../i18n/I18nWrapper";
import injectIntl from "../utils/injectIntl";
import NavSearch from "./main/NavSearch"
import ProfileController from "./profile/ProfileController";
import UserStore from "../stores/UserStore";
import Vocabulary from "../constants/Vocabulary";

class MainView extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            showProfile: false
        };
    }

    componentWillMount() {
        I18nStore.setIntl(this.props.intl);
        this.unsubscribe = UserStore.listen(this._onUserLoaded);
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    _onUserLoaded = (data) => {
        if (data.action === Actions.loadUser)
            this.forceUpdate();
    };

    _openUserProfile = () => {
        this.setState({showProfile: true});
    };

    _closeUserProfile = () => {
        this.setState({showProfile: false});
    };

    _onSelectLang = (lang) => {
        I18nStore.setActiveLanguage(lang);
        window.location.reload();
    };

    render() {
        if (!UserStore.isLoaded()) {
            return <div>{this.props.children}</div>;
        }
        const user = UserStore.getCurrentUser(),
            name = user.firstName.substr(0, 1) + '. ' + user.lastName;
        return <div>
            <header>
                <Navbar fluid={true}>
                    <Navbar.Header>
                        <Navbar.Brand>{Constants.APP_NAME}</Navbar.Brand>
                    </Navbar.Header>
                    <Nav>
                        <LinkContainer
                            to='dashboard'><NavItem>{this.i18n('main.dashboard-nav')}</NavItem></LinkContainer>
                        <LinkContainer to='reports'><NavItem>{this.i18n('main.reports-nav')}</NavItem></LinkContainer>
                        <LinkContainer
                            to='statistics'><NavItem>{this.i18n('main.statistics-nav')}</NavItem></LinkContainer>
                        <IfGranted expected={Vocabulary.ROLE_ADMIN} actual={user.types} element='span'>
                            <LinkContainer to='admin'><NavItem>{this.i18n('main.admin-nav')}</NavItem></LinkContainer>
                        </IfGranted>
                    </Nav>
                    <Nav pullRight style={{margin: '0 -15px 0 0'}}>
                        <li>
                            <NavSearch/>
                        </li>
                        <li>
                            {this._renderLanguageSelector()}
                        </li>
                        <NavDropdown id='logout' title={name}>
                            <MenuItem onClick={this._openUserProfile}>{this.i18n('main.user-profile')}</MenuItem>
                            <MenuItem divider/>
                            <MenuItem href='#' onClick={Authentication.logout}>{this.i18n('main.logout')}</MenuItem>
                        </NavDropdown>
                    </Nav>
                </Navbar>
            </header>
            <section style={{height: '100%'}}>
                <ProfileController show={this.state.showProfile} onClose={this._closeUserProfile}/>
                {this.props.children}
            </section>
        </div>;
    }

    _renderLanguageSelector() {
        const csCls = classNames("lang", {"selected": I18nStore.getActiveLanguage() === Constants.LANG.CS}),
            enCls = classNames("lang", {"selected": I18nStore.getActiveLanguage() === Constants.LANG.EN});
        return <div className="lang">
            <a className={csCls} href="#"
               onClick={() => this._onSelectLang(Constants.LANG.CS)}>CS</a>
            &nbsp;/&nbsp;
            <a className={enCls} href="#"
               onClick={() => this._onSelectLang(Constants.LANG.EN)}>EN</a>
        </div>;
    }
}

export default injectIntl(I18nWrapper(MainView));
