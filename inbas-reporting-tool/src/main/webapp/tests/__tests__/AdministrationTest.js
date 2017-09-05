import React from "react";
import TestUtils from "react-addons-test-utils";
import Environment from "../environment/Environment";
import Generator from "../environment/Generator";
import Actions from "../../js/actions/Actions";
import Administration from "../../js/components/admin/Administration";
import UserStore from "../../js/stores/UserStore";
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
            usersComp = TestUtils.scryRenderedComponentsWithType(component, require('../../js/components/admin/UsersController').default);
        expect(usersComp.length).toEqual(0);
    });

    it('displays users if current user is admin', () => {
        const user = Generator.generatePerson();
        user.types = [Vocabulary.ROLE_ADMIN];
        spyOn(UserStore, 'getCurrentUser').and.returnValue(user);

        const component = Environment.render(<Wrapper><Administration/></Wrapper>),
            usersComp = TestUtils.findRenderedComponentWithType(component, require('../../js/components/admin/UsersController').default);
        expect(usersComp).not.toBeNull();
    });
});
