import Actions from "../actions/Actions";
import Ajax from "./Ajax";
import Logger from "./Logger";
import Routes from "./Routes";
import Routing from "./Routing";

export default class Authentication {

    static login(username, password, errorCallback) {
        Ajax.post('j_spring_security_check', null, 'form')
            .send('username=' + username).send('password=' + password)
            .end((err, resp) => {
                if (err) {
                    errorCallback();
                    return;
                }
                const status = JSON.parse(resp.text);
                if (!status.success || !status.loggedIn) {
                    errorCallback(status);
                    return;
                }
                Actions.loadUser();
                Logger.log('User successfully authenticated.');
                Routing.transitionToOriginalTarget();
            });
    }

    static logout() {
        Ajax.post('j_spring_security_logout').end(function (err) {
            if (err) {
                Logger.error('Logout failed. Status: ' + err.status);
            } else {
                Logger.log('User successfully logged out.');
            }
            Routing.transitionTo(Routes.login);
            window.location.reload();
        });
    }
}
