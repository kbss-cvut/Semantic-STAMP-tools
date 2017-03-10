'use strict';

import React from "react";
import {Button, MenuItem} from "react-bootstrap";
import JsonLdUtils from "jsonld-utils";
import Actions from "../../actions/Actions";
import injectIntl from "../../utils/injectIntl";
import OptionsStore from "../../stores/OptionsStore";
import Vocabulary from "../../constants/Vocabulary";

class PhaseTransition extends React.Component {
    static propTypes = {
        report: React.PropTypes.object.isRequired,
        onLoading: React.PropTypes.func.isRequired,
        onSuccess: React.PropTypes.func.isRequired,
        onError: React.PropTypes.func.isRequired,
        menuItem: React.PropTypes.bool
    };

    static defaultProps = {
        menuItem: false
    };

    constructor(props) {
        super(props);
        this.state = {
            phases: OptionsStore.getOptions('reportingPhase')
        };
    }

    componentDidMount() {
        if (this.state.phases.length === 0) {
            Actions.loadOptions('reportingPhase');
            this.unsubscribe = OptionsStore.listen(this._onPhasesLoaded);
        }
    }

    componentWillUnmount() {
        if (this.unsubscribe) {
            this.unsubscribe();
        }
    }

    _onPhasesLoaded = (type, data) => {
        if (type == 'reportingPhase') {
            this.setState({phases: data});
        }
    };

    _onPhaseTransition = () => {
        this.props.onLoading();
        Actions.phaseTransition(this.props.report, this.props.onSuccess, this.props.onError);
    };

    _determinePhase() {
        const reportPhase = this.props.report.phase;
        return this.state.phases.find((item) => {
            return item['@id'] === reportPhase;
        });
    }

    render() {
        const phase = this._determinePhase();
        if (phase && phase[Vocabulary.TRANSITION_LABEL]) {
            if (!this.props.menuItem) {
                return <Button bsStyle='primary' bsSize='small'
                               onClick={this._onPhaseTransition}>{JsonLdUtils.getLocalized(phase[Vocabulary.TRANSITION_LABEL], this.props.intl)}</Button>;
            } else {
                return <MenuItem
                    onClick={this._onPhaseTransition}>{JsonLdUtils.getLocalized(phase[Vocabulary.TRANSITION_LABEL], this.props.intl)}</MenuItem>;
            }
        } else {
            return null;
        }
    }
}

export default injectIntl(PhaseTransition);
