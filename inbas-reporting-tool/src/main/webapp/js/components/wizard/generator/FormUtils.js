'use strict';

import Constants from "../../../constants/Constants";
import Utils from "../../../utils/Utils";

export default class FormUtils {
    
    static isForm(structure) {
        return Utils.hasValue(structure, Constants.FORM.LAYOUT_CLASS, Constants.FORM.LAYOUT.FORM);
    }
    
    static isWizardStep(structure) {
        return Utils.hasValue(structure, Constants.FORM.LAYOUT_CLASS, Constants.FORM.LAYOUT.WIZARD_STEP);
    }

    static isSection(question) {
        return Utils.hasValue(question, Constants.FORM.LAYOUT_CLASS, Constants.FORM.LAYOUT.QUESTION_SECTION);
    }

    static isTypeahead(question) {
        return Utils.hasValue(question, Constants.FORM.LAYOUT_CLASS, Constants.FORM.LAYOUT.QUESTION_TYPEAHEAD);
    }
    
    static getPossibleValuesQuery(question) {
        return Utils.getJsonAttValue(question, Constants.FORM.HAS_OPTIONS_QUERY);
    }

    static isDisabled(question) {
        return Utils.hasValue(question, Constants.FORM.LAYOUT_CLASS, Constants.FORM.LAYOUT.DISABLED);
    }

    static isHidden(question) {
        return Utils.hasValue(question, Constants.FORM.LAYOUT_CLASS, Constants.FORM.LAYOUT.HIDDEN);
    }
}
