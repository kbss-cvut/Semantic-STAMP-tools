'use strict';

import React from "react";
import Aircraft from "./Aircraft";
import OccurrenceClassification from "./OccurrenceClassification";
import OccurrenceDetail from "./Occurrence";

const BasicOccurrenceInfo = (props) => {
    return <div>
        <OccurrenceDetail report={props.report} onChange={props.onChange}/>

        <OccurrenceClassification onChange={props.onChange} report={props.report}/>

        <Aircraft aircraft={props.report.occurrence.aircraft} onChange={props.onChange}/>
    </div>;
};

BasicOccurrenceInfo.propTypes = {
    report: React.PropTypes.object.isRequired,
    onChange: React.PropTypes.func.isRequired
};

export default BasicOccurrenceInfo;
