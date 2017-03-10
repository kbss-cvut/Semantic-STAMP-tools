'use strict';

import React from "react";
import Select from "../../Select";

const OPTION_LABEL_MAX_LENGTH = 150;

export default class ExistingMeasureSelector extends React.Component {
    static propTypes = {
        audit: React.PropTypes.object.isRequired,
        finding: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.measures = this._getMeasuresForSelect();
    }

    _getMeasuresForSelect() {
        var measures = [],
            knownMeasures = this.props.finding.correctiveMeasures ? this.props.finding.correctiveMeasures : [],
            finding;
        for (var i = 0, len = this.props.audit.findings.length; i < len; i++) {
            finding = this.props.audit.findings[i];
            if (finding === this.props.finding || !finding.correctiveMeasures) {
                continue;
            }
            for (var j = 0, lenn = finding.correctiveMeasures.length; j < lenn; j++) {
                if (knownMeasures.indexOf(finding.correctiveMeasures[j]) === -1) {
                    measures.push(finding.correctiveMeasures[j]);
                }
            }
        }
        return measures;
    }

    _onChange = (e) => {
        var index = Number(e.target.value);
        this.props.onChange(this.measures[index]);
    };

    render() {
        return <Select options={this._generateOptions()} onChange={this._onChange} addDefault={true}/>;
    }

    _generateOptions() {
        var options = [];
        for (var i = 0, len = this.measures.length; i < len; i++) {
            options.push({
                value: i,
                label: this.measures[i].description.length > OPTION_LABEL_MAX_LENGTH ? this.measures[i].description.substring(OPTION_LABEL_MAX_LENGTH) : this.measures[i].description,
                title: this.measures[i].description
            });
        }
        return options;
    }
}
