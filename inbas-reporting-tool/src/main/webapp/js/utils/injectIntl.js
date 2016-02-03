'use strict';

var injectIntl = require('react-intl').injectIntl;

/**
 * Our version of react-intl's injectIntl.
 *
 * Decorates the basic instance returned by injectIntl with accessors to the wrapped element.
 * @param component
 */
module.exports = function (component) {
    var comp = injectIntl(component);
    comp.prototype.getWrappedElement = function () {
        return this.refs.wrappedElement;
    };
    // Store this only for development purposes
    if (process.env.NODE_ENV !== 'production') {
        comp.wrappedComponent = component;
    }
    return comp;
};
