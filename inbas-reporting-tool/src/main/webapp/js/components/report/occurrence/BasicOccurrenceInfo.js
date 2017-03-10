'use strict';

import React from "react";
import assign from "object-assign";
import Aircraft from "./Aircraft";
import OccurrenceClassification from "./OccurrenceClassification";
import OccurrenceDetail from "./Occurrence";

const BasicOccurrenceInfo = (props) => {
    return <div>
        <OccurrenceDetail report={props.report} onChange={props.onChange}/>

        <OccurrenceClassification onChange={props.onChange} report={props.report}/>

        <Aircraft aircraft={props.report.occurrence.aircraft} onChange={change => {
            const occurrence = assign({}, props.report.occurrence, change);
            props.onChange({occurrence: occurrence});
        }}/>
    </div>;
};

BasicOccurrenceInfo.propTypes = {
    report: React.PropTypes.object.isRequired,
    onChange: React.PropTypes.func.isRequired
};

export default BasicOccurrenceInfo;
