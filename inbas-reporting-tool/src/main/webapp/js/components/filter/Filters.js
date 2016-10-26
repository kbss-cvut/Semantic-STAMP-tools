'use strict';

import React from "react";
import {Button, ButtonToolbar} from "react-bootstrap";
import JsonLdUtils from "jsonld-utils";
import Actions from "../../actions/Actions";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import OptionsStore from "../../stores/OptionsStore";

class Filters extends React.Component {
    static propTypes = {
        filters: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired,
        onResetFilters: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            occurrenceCategory: JsonLdUtils.processTypeaheadOptions(OptionsStore.getOptions('occurrenceCategory'))
        }
    }

    componentDidMount() {
        Actions.loadOptions('occurrenceCategory');
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type, options) => {
        var update = {};
        update[type] = JsonLdUtils.processTypeaheadOptions(options);
        this.setState(update);
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    render() {
        return <div>
            <div>
                Occurrence category filter
            </div>
            <div className='row filters-footer'>
                <ButtonToolbar className='float-right'>
                    <Button bsSize='small' bsStyle='primary'
                            onClick={this.props.onResetFilters}>{this.i18n('reports.filter.reset')}</Button>
                </ButtonToolbar>
            </div>
        </div>;
    }
}

export default injectIntl(I18nWrapper(Filters));
