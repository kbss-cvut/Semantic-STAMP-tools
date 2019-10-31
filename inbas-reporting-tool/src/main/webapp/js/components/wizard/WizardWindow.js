import React from "react";
import PropTypes from "prop-types";
import {Modal} from "react-bootstrap";
import assign from "object-assign";

import Wizard from "./Wizard";

const WizardWindow = (props) => {
    const properties = assign({}, props, {onClose: props.onHide}),
        modalProps = assign({}, props);
    delete modalProps.steps;
    delete modalProps.onFinish;
    delete modalProps.start;
    delete modalProps.readOnly;
    delete modalProps.enableForwardSkip;
    delete modalProps.title;

    return <Modal {...modalProps} show={props.show} bsSize="large" animation={true} dialogClassName="large-modal">
        <Modal.Header closeButton>
            <Modal.Title>{props.title}</Modal.Title>
        </Modal.Header>

        <div className="modal-body" style={{overflow: 'hidden'}}>
            <Wizard {...properties}/>
        </div>
    </Modal>;
};

WizardWindow.propTypes = {
    onHide: PropTypes.func,
    title: PropTypes.string,
    show: PropTypes.bool
};

export default WizardWindow;
