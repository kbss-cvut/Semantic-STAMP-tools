'use strict';

import React from "react";
import {ClipLoader} from "halogen";
import I18Store from "../stores/I18nStore";

const InlineMask = (props) => {
    const text = props.text ? props.text : I18Store.i18n('please-wait');
    return <div className={props.classes ? props.classes : 'inline-mask'}>
        <div className='spinner-container'>
            <div style={{width: 24, height: 24, margin: 'auto'}}>
                <ClipLoader color='#337ab7' size='24px'/>
            </div>
            <div className='spinner-message'>{text}</div>
        </div>
    </div>;
};

InlineMask.propTypes = {
    text: React.PropTypes.string,
    classes: React.PropTypes.string
};

export default InlineMask;
