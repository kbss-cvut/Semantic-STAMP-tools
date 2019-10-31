import React from "react";
import {IfGranted} from "react-authorization";
import UsersController from "./UsersController";
import UserStore from "../../stores/UserStore";
import Vocabulary from "../../constants/Vocabulary";

const Administration = () => {
    // Wrap it all up in IfGranted to make sure that unauthorized users are not able to view anything even if they are able
    // to get the component to render (e.g. by setting route URL manually)

    const user = UserStore.getCurrentUser();
    return <IfGranted expected={Vocabulary.ROLE_ADMIN} actual={user ? user.types : []}>
        <UsersController/>
    </IfGranted>;
};

export default Administration;
