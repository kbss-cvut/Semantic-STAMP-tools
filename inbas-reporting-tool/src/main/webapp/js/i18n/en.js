/**
 * English localization.
 */

var Constants = require('../constants/Constants');

module.exports = {
    'locales': ['en'],

    'messages': {
        'add': 'Add',
        'back': 'Go Back',
        'cancel': 'Cancel',
        'cancel-tooltip': 'Discard changes',
        'save': 'Save',
        'delete': 'Delete',
        'headline': 'Headline',
        'narrative': 'Narrative',
        'table-actions': 'Actions',
        'table-edit': 'Edit',
        'save-success-message': 'Report successfully saved.',
        'save-failed-message': 'Unable to save report. Server responded with message: ',
        'author': 'Author',
        'author-title': 'Report author',
        'login': {
            'title': Constants.APP_NAME + ' - Login',
            'username': 'Username',
            'password': 'Password',
            'submit': 'Login',
            'register': 'Register',
            'error': 'Authentication failed.'
        },
        'register': {
            'title': Constants.APP_NAME + ' - Registration',
            'first-name': 'First name',
            'last-name': 'Last name',
            'username': 'Username',
            'password': 'Password',
            'password-confirm': 'Confirm password',
            'passwords-not-matching-tooltip': 'Passwords don\'t match',
            'submit': 'Register',
            'mask': 'Registering...'
        },
        'main': {
            'dashboard-nav': 'Dashboard',
            'preliminary-nav': 'Preliminary Reports',
            'investigations-nav': 'Investigations',
            'logout': 'Logout'
        },
        'dashboard': {
            'welcome': 'Hello {name}, Welcome to the ' + Constants.APP_NAME + '.',
            'create-tile': 'Create Occurrence Report',
            'search-tile': 'Search for Occurrence Case',
            'search-placeholder': 'Occurrence headline',
            'view-all-tile': 'View All Occurrences',
            'create-empty-tile': 'Start with Empty Report',
            'create-import-tile': 'Import Initial Report',
            'recent-panel-heading': 'Recently Edited/Added Reports',
            'recent-table-headline': 'Occurrence headline',
            'recent-table-date': 'Occurrence date',
            'recent-table-last-edited': 'Last edited'
        },
        'reports': {
            'no-occurrence-reports': 'There are no occurrence reports, yet.',
            'no-reports': 'There are no reports here, yet.',
            'open-tooltip': 'Click to see report detail',
            'edit-tooltip': 'Edit this occurrence report',
            'delete-tooltip': 'Delete this occurrence report',
            'loading-mask': 'Loading reports...',
            'panel-title': 'Occurrence reports',
            'table-date': 'Occurrence date',
            'table-type': 'Type'
        },
        'delete-dialog': {
            'title': 'Delete {type} Report?',
            'content': 'Are you sure you want to remove this report?'
        },
        'occurrence': {
            'headline-tooltip': 'Short descriptive summary of the occurrence - this field is required',
            'start-time': 'Occurrence start',
            'start-time-tooltip': 'Date and time when the event occurred',
            'end-time': 'Occurrence end',
            'end-time-tooltip': 'Date and time when the event ended',
            'class': 'Occurrence class',
            'class-tooltip': 'Occurrence class - this field is required'
        },
        'initial': {
            'panel-title': 'Initial reports',
            'table-report': 'Report',
            'wizard-add-title': 'Add initial report',
            'wizard-edit-title': 'Edit initial report'
        },
        'preliminary': {
            'panel-title': 'Preliminary reports',
            'table-investigate': 'Investigate',
            'table-investigate-tooltip': 'Investigate this occurrence',
            'detail': {
                'loading-mask': 'Loading report...',
                'panel-title': 'Preliminary occurrence report',
                'save-tooltip': 'Save changes',
                'saving': 'Saving...',
                'invalid-tooltip': 'Some of the required values are missing',
                'last-edited-msg': 'Last edited {date} by {name}.',
                'narrative-tooltip': 'Narrative - this field is required'
            }
        }
    }
};
