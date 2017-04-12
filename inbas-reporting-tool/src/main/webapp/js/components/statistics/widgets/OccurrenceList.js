'use strict';

import React from "react";
import FrequencyList from "./FrequencyList";

const OccurrenceList = () => {
    return <FrequencyList query="occurrence_categories_top_yearback" fromColor="red" toColor="lemonChiffon"/>;
};

export default OccurrenceList;