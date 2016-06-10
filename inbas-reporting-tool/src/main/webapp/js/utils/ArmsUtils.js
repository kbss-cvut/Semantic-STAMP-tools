'use strict';

const ARMS_THRESHOLDS = {
    green: 20,
    yellow: 500
};

export default class ArmsUtils {
    
    static resolveArmsIndexClass(armsIndex) {
        if (!armsIndex) {
            return '';
        }
        if (armsIndex < ARMS_THRESHOLDS.green) {
            return 'arms-index-green';
        }
        if (armsIndex < ARMS_THRESHOLDS.yellow) {
            return 'arms-index-yellow';
        }
        if (armsIndex >= ARMS_THRESHOLDS.yellow) {
            return 'arms-index-red';
        }
    }
}
