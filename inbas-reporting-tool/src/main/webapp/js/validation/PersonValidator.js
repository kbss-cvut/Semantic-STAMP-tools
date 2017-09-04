export default class PersonValidator {

    /**
     * Checks whether the specified person instance is valid, i.e. it contains all the required attribute values.
     * @param person The instance to check
     * @param withPassword Whether to include password presence and match to confirmation in the validation
     */
    static isValid(person, withPassword = true) {
        if (!person.firstName || !person.lastName || !person.username) {
            return false;
        }
        if (withPassword && (!person.password || person.password !== person.passwordConfirm)) {
            return false;
        }
        return true;
    }
}