import React from "react";
import PropTypes from "prop-types";
import {Alert, Button} from "react-bootstrap";
import {FormattedMessage} from "react-intl";

import I18nWrapper from "../i18n/I18nWrapper";
import injectIntl from "../utils/injectIntl";
import Routing from "../utils/Routing";

/**
 * Shows alert with message informing that a resource could not be found.
 *
 * Closing the alert transitions the user to the application's home.
 */
const ResourceNotFound = (props) => {
    const onClose = () => Routing.transitionToHome();
    let text;
    if (props.identifier) {
        text = <FormattedMessage id='notfound.msg-with-id'
                                 values={{resource: props.resource, identifier: props.identifier}}/>;
    } else {
        text = <FormattedMessage id='notfound.msg' values={{resource: props.resource}}/>;
    }
    return <Alert bsStyle='danger' onDismiss={onClose}>
        <h4>{props.i18n('notfound.title')}</h4>

        <p>{text}</p>

        <p>
            <Button onClick={onClose}>{props.i18n('close')}</Button>
        </p>
    </Alert>;
};

ResourceNotFound.propTypes = {
    resource: PropTypes.string.isRequired,
    identifier: PropTypes.object
};

export default injectIntl(I18nWrapper(ResourceNotFound));
