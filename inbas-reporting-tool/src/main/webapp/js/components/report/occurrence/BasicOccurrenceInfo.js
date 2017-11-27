import React from "react";
import PropTypes from "prop-types";
import OccurrenceClassification from "./OccurrenceClassification";
import OccurrenceDetail from "./Occurrence";

const BasicOccurrenceInfo = (props) => {
    return <div>
        <OccurrenceDetail report={props.report} onChange={props.onChange}/>

        <OccurrenceClassification onChange={props.onChange} report={props.report}/>
    </div>;
};

BasicOccurrenceInfo.propTypes = {
    report: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
};

export default BasicOccurrenceInfo;
