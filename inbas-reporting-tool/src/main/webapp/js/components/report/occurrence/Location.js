import React from "react";
import PropTypes from "prop-types";
import Input from "../../Input";
import assign from "object-assign";

import Constants from "../../../constants/Constants";
import I18nWrapper from "../../../i18n/I18nWrapper";
import injectIntl from "../../../utils/injectIntl";

class Location extends React.Component {
    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        const location = props.occurrence.location;
        this.state = {
            named: !location || location.javaClass === Constants.NAMED_LOCATION_JAVA_CLASS
        };
    }

    _onLocationTypeChange = () => {
        this.setState({named: !this.state.named});
    };

    _onLocationNameChange = (e) => {
        const name = e.target.value,
            update = assign({}, this.props.occurrence);
        update.location = assign({}, update.location, {
            javaClass: Constants.NAMED_LOCATION_JAVA_CLASS,
            name: name
        });
        delete update.location.latitude;
        delete update.location.longitude;
        this.props.onChange({occurrence: update});
    };

    _onLocationGpsChange = (change) => {
        const update = assign({}, this.props.occurrence);
        update.location = assign({}, update.location, change);
        update.location.javaClass = Constants.GPS_LOCATION_JAVA_CLASS;
        delete update.location.name;
        this.props.onChange({occurrence: update});
    };

    render() {
        const i18n = this.i18n;
        return <div>
            <label className='control-label'>{i18n('occurrence.location.title')}</label>
            <div className="row">
                <div className="col-xs-2">
                    <Input type="radio" label={i18n('occurrence.location.named')} checked={this.state.named}
                           value='named' onChange={this._onLocationTypeChange}/>
                </div>
                <div className="col-xs-2">
                    <Input type="radio" label={i18n('occurrence.location.gps')} checked={!this.state.named}
                           value='gps' onChange={this._onLocationTypeChange}/>
                </div>
            </div>
            {this._renderLocationInput()}
        </div>;
    }

    _renderLocationInput() {
        if (this.state.named) {
            return this._renderNamedLocation();
        } else {
            return this._renderGPSLocation();
        }
    }

    _renderNamedLocation() {
        const location = this.props.occurrence.location;
        return <div className="row">
            <div className="col-xs-4">
                <Input type="text" bsSize="small" label={this.i18n('occurrence.location.named.name')}
                       value={location && location.name ? location.name : ''} onChange={this._onLocationNameChange}/>
            </div>
        </div>;
    }

    _renderGPSLocation() {
        const location = this.props.occurrence.location,
            latValue = location && location.latitude ? location.latitude : '',
            longValue = location && location.longitude ? location.longitude : '';
        return <div className="row">
            <div className="col-xs-2">
                <Input type="number" min="-90" max="90" step="0.0000001" bsSize="small"
                       label={this.i18n('occurrence.location.gps.lat')}
                       value={latValue}
                       validation={latValue.length === 0 ? "error" : undefined}
                       title={latValue.length === 0 ? this.i18n('occurrence.location.gps.lat.empty') : undefined}
                       onChange={e => {
                           this._onLocationGpsChange({latitude: Number(e.target.value)})
                       }}/>
            </div>
            <div className="col-xs-2">
                <Input type="number" min="-180" max="180" step="0.0000001" bsSize="small"
                       label={this.i18n('occurrence.location.gps.long')}
                       value={longValue}
                       validation={longValue.length === 0 ? "error" : undefined}
                       title={longValue.length === 0 ? this.i18n('occurrence.location.gps.long.empty') : undefined}
                       onChange={e => {
                           this._onLocationGpsChange({longitude: Number(e.target.value)})
                       }}/>
            </div>
        </div>;
    }
}

Location.propTypes = {
    occurrence: PropTypes.object.isRequired,
    onChange: PropTypes.func.isRequired
};

export default injectIntl(I18nWrapper(Location));
