/**
 * English localization.
 */

var Constants = require('../constants/Constants');

module.exports = {
    'locales': ['en'],

    'messages': {
        'back': 'Go Back',
        'cancel': 'Cancel',
        'table-actions': 'Actions',
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
            'table-headline': 'Headline',
            'table-date': 'Occurrence date',
            'table-narrative': 'Narrative',
            'table-type': 'Type'
        },
        'delete': {
            'title': 'Delete {type} Report?',
            'content': 'Are you sure you want to remove this report?',
            'submit': 'Delete'
        }
    }
};
