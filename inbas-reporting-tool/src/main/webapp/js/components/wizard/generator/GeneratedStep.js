'use strict';

import React from "react";
import assign from "object-assign";
import Question from "./Question";

export default class GeneratedStep extends React.Component {
    static propTypes = {
        stepData: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            question: this.props.store.stepData[this.props.stepIndex]
        };
    }

    onChange = (index, change) => {
        var newState = assign(this.state.question, change);
        this.setState({question: newState});
        this.props.store.stepData[this.props.stepIndex] = newState;
    };

    getData = () => {
        return this.state.question;
    };

    render() {
        return <Question question={this.state.question} onChange={this.onChange} withoutPanel={true}/>;
    }
}
