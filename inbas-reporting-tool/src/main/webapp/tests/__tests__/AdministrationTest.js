import React from "react";
import TestUtils from "react-addons-test-utils";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Actions from "../../js/actions/Actions";
import Administration from "../../js/components/admin/Administration";
import UserStore from "../../js/stores/UserStore";
import UsersController from "../../js/components/admin/UsersController";
import Vocabulary from "../../js/constants/Vocabulary";

class Wrapper extends React.Component {
    render() {
        return this.props.children;
    }
}

describe('Administration', () => {

    beforeEach(() => {
        spyOn(Actions, 'loadUser');
        spyOn(Actions, 'loadUsers');
    });

    it('does not display users if current user is not admin', () => {
        const user = Generator.generatePerson();
        user.types = [];
        spyOn(UserStore, 'getCurrentUser').and.returnValue(user);

        const component = Environment.render(<Wrapper><Administration/></Wrapper>),
            usersComp = TestUtils.scryRenderedComponentsWithType(component, UsersController);
        expect(usersComp.length).toEqual(0);
    });

    it('displays users if current user is admin', () => {
        const user = Generator.generatePerson();
        user.types = [Vocabulary.ROLE_ADMIN];
        spyOn(UserStore, 'getCurrentUser').and.returnValue(user);

        const component = Environment.render(<Wrapper><Administration/></Wrapper>),
            usersComp = TestUtils.findRenderedComponentWithType(component, UsersController);
        expect(usersComp).not.toBeNull();
    });

    it('indicates account status by appropriate icon', () => {
        const user = Generator.generatePerson();
        user.types = [Vocabulary.ROLE_ADMIN];
        const users = [user, {
            uri: Generator.getRandomUri(),
            firstName: 'locked',
            lastName: 'user',
            username: 'locked@inbas.cz',
            types: [Vocabulary.LOCKED]
        }, {
            uri: Generator.getRandomUri(),
            firstName: 'disabled',
            lastName: 'user',
            username: 'disabled@inbas.cz',
            types: [Vocabulary.DISABLED]
        }];
        spyOn(UserStore, 'getCurrentUser').and.returnValue(user);


        const component = Environment.render(<Wrapper><Administration/></Wrapper>),
            controller = TestUtils.findRenderedComponentWithType(component, UsersController);
        controller._onUsersLoaded({action: Actions.loadUsers, users: users});

        const statusIcons = TestUtils.scryRenderedComponentsWithType(controller, require("react-bootstrap").Glyphicon);
        expect(statusIcons.length).toEqual(3);
        expect(statusIcons[0].props.glyph).toEqual('ok');
        expect(statusIcons[1].props.glyph).toEqual('ban-circle');
        expect(statusIcons[2].props.glyph).toEqual('minus');
    });
});
