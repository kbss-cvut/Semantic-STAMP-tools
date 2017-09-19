import React from "react";
import PropTypes from "prop-types";
import {ClipLoader} from "halogen";
import classNames from "classnames";
import I18Store from "../stores/I18nStore";

const Mask = (props) => {
    const text = props.text ? props.text : I18Store.i18n('please-wait'),
        containerClasses = classNames('spinner-container', {'without-text': props.withoutText});
    return <div className={props.classes ? props.classes : 'mask'}>
        <div className={containerClasses}>
            <div style={{width: 32, height: 32, margin: 'auto'}}>
                <ClipLoader color='#337ab7' size='32px'/>
            </div>
            {!props.withoutText && <div className='spinner-message'>{text}</div>}
        </div>
    </div>;
};

Mask.propTypes = {
    text: PropTypes.string,
    classes: PropTypes.string,
    withoutText: PropTypes.bool
};

Mask.defaultProps = {
    withoutText: false
};

export default Mask;
