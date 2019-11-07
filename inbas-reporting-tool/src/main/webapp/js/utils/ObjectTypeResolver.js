'use strict';

import OptionStore from "../stores/OptionsStore";
import Constants from "../constants/Constants";

module.exports = {

    /**
     * Resolves type of the specified object.
     *
     * More precisely it returns an option whose identifier corresponds to the object's type.
     * @param object Object with types field or type URI as string
     * @param options The options to search for type
     * @return {*} Matching type or null
     */
    resolveType: function (object, options) {
        if(options){
            if(options.optionTypes) {
                return this.resolveTypeFromOptionType(object, options.optionTypes);
            }else {
                return this.resolveTypeFromEventTypes(object, options);
            }
        }
        return null;
    },
    resolveTypeFromEventTypes: function (object, options) {
        if (!object || !options) {
            return null;
        }
        let types = this.getTypes(object);
        return this.findAnyOption(types, options);
    },

    getTypes: function(object){
        return typeof object === 'object' ? (Array.isArray(object) ? object : object.types) : [object];
    },

    findAnyOption: function(types, options){
        let tLen = types.length, j;
        for (let i = 0, len = options.length; i < len; i++) {
            let option = options[i];
            for (j = 0; j < tLen; j++) {
                if (types.indexOf(option['@id']) !== -1) {
                    return option;
                }
            }
        }
        return null;
    },

    resolveTypeFromOptionType(object, optionTypes){
        let types = this.getTypes(object);
        if(!optionTypes)// hack - default option types to resolve from, fixes tests tests/__tests__/FactorRendererTest.js:77:24
            optionTypes = [Constants.OPTIONS.EVENT_TYPE, Constants.OPTIONS.LOSS_EVENT_TYPE];
        if(!optionTypes.length)
            optionTypes = [optionTypes];

        for(let i = 0, len = optionTypes.length; i < len; i ++){
            let optionType = optionTypes[i];
            let options = OptionStore.getOptions(optionType);
            let option = this.findAnyOption(types, options);
            if(option)
                return option;
        }
        return null;
    },

    resolveEventType: function (object){

    }
};
