import React from "react";
import PropTypes from "prop-types";
import Typeahead from "react-bootstrap-typeahead";
import JsonLdUtils from "jsonld-utils";

import Actions from "../../actions/Actions";
import Constants from "../../constants/Constants";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import OptionsStore from "../../stores/OptionsStore";
import EventTypeTypeaheadResultList from "./EventTypeTypeaheadResultList";

class EventTypeTypeahead extends React.Component {

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            options: []
        };
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this.onEventsLoaded);
        Actions.loadOptions(Constants.OPTIONS.EVENT_TYPE);
        if (this.props.focus) {
            this.focus();
        }
    }

    onEventsLoaded = (type, data) => {
        if (type === Constants.OPTIONS.EVENT_TYPE) {
            this.setState({options: JsonLdUtils.processTypeaheadOptions(data)});
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    focus() {
        this.eventTypeSelect.focus();
    }

    render() {
        const value = this.props.value ? this.props.value : null,
            placeholder = this.props.placeholder ? this.props.placeholder : this.i18n('eventtype.title');

        return <Typeahead ref={c => this.eventTypeSelect = c} label={this.props.label} name='eventType' size='small'
                          formInputOption='id' placeholder={placeholder} onOptionSelected={this.props.onSelect}
                          filterOption='name' value={value} displayOption='name' options={this.state.options}
                          customListComponent={EventTypeTypeaheadResultList} disabled={this.props.disabled}/>;
    }
}

EventTypeTypeahead.propTypes = {
    onSelect: PropTypes.func.isRequired,
    focus: PropTypes.bool,
    placeholder: PropTypes.string,
    value: PropTypes.string
};

EventTypeTypeahead.defaultProps = {
    focus: false
};

export default injectIntl(I18nWrapper(EventTypeTypeahead));
