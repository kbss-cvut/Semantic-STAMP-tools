/**
 * English localization.
 */

var Constants = require('../constants/Constants');

module.exports = {
    'locale': 'en',

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
        'description': 'Description',

        'login.title': Constants.APP_NAME + ' - Login',
        'login.username': 'Username',
        'login.password': 'Password',
        'login.submit': 'Login',
        'login.register': 'Register',
        'login.error': 'Authentication failed.',
        'login.progress-mask': 'Logging in...',

        'register.title': Constants.APP_NAME + ' - Registration',
        'register.first-name': 'First name',
        'register.last-name': 'Last name',
        'register.username': 'Username',
        'register.password': 'Password',
        'register.password-confirm': 'Confirm password',
        'register.passwords-not-matching-tooltip': 'Passwords don\'t match',
        'register.submit': 'Register',
        'register.mask': 'Registering...',

        'main.dashboard-nav': 'Dashboard',
        'main.preliminary-nav': 'Preliminary Reports',
        'main.investigations-nav': 'Investigations',
        'main.logout': 'Logout',

        'dashboard.welcome': 'Hello {name}, Welcome to the ' + Constants.APP_NAME + '.',
        'dashboard.create-tile': 'Create Occurrence Report',
        'dashboard.search-tile': 'Search for Occurrence Case',
        'dashboard.search-placeholder': 'Occurrence headline',
        'dashboard.view-all-tile': 'View All Occurrences',
        'dashboard.create-empty-tile': 'Start with Empty Report',
        'dashboard.create-import-tile': 'Import Initial Report',
        'dashboard.recent-panel-heading': 'Recently Edited/Added Reports',
        'dashboard.recent-table-headline': 'Occurrence headline',
        'dashboard.recent-table-date': 'Occurrence date',
        'dashboard.recent-table-last-edited': 'Last edited',

        'reports.no-occurrence-reports': 'There are no occurrence reports, yet.',
        'reports.no-reports': 'There are no reports here, yet.',
        'reports.open-tooltip': 'Click to see report detail',
        'reports.edit-tooltip': 'Edit this occurrence report',
        'reports.delete-tooltip': 'Delete this occurrence report',
        'reports.loading-mask': 'Loading reports...',
        'reports.panel-title': 'Occurrence reports',
        'reports.table-date': 'Occurrence date',
        'reports.table-type': 'Type',

        'delete-dialog.title': 'Delete {type} Report?',
        'delete-dialog.content': 'Are you sure you want to remove this report?',

        'occurrence.headline-tooltip': 'Short descriptive summary of the occurrence - this field is required',
        'occurrence.start-time': 'Occurrence start',
        'occurrence.start-time-tooltip': 'Date and time when the event occurred',
        'occurrence.end-time': 'Occurrence end',
        'occurrence.end-time-tooltip': 'Date and time when the event ended',
        'occurrence.class': 'Occurrence class',
        'occurrence.class-tooltip': 'Occurrence class - this field is required',

        'initial.panel-title': 'Initial reports',
        'initial.table-report': 'Report',
        'initial.wizard-add-title': 'Add initial report',
        'initial.wizard-edit-title': 'Edit initial report',
        'initial.label': 'Initial report',

        'preliminary.panel-title': 'Preliminary reports',
        'preliminary.table-investigate': 'Investigate',
        'preliminary.table-investigate-tooltip': 'Investigate this occurrence',
        'preliminary.detail.loading-mask': 'Loading report...',
        'preliminary.detail.panel-title': 'Preliminary occurrence report',
        'preliminary.detail.save-tooltip': 'Save changes',
        'preliminary.detail.saving': 'Saving...',
        'preliminary.detail.invalid-tooltip': 'Some of the required values are missing',
        'preliminary.detail.last-edited-msg': 'Last edited {date} by {name}.',
        'preliminary.detail.narrative-tooltip': 'Narrative - this field is required',
        'preliminary.detail.table-edit-tooltip': 'Edit statement',
        'preliminary.detail.table-delete-tooltip': 'Delete statement',
        'preliminary.detail.corrective.panel-title': 'Corrective Measures',
        'preliminary.detail.corrective.table-description': 'Corrective Measure',
        'preliminary.detail.corrective.description-placeholder': 'Corrective measure description',
        'preliminary.detail.corrective.add-tooltip': 'Add a corrective measure',
        'preliminary.detail.corrective.wizard-title': 'Corrective Measure Wizard',
        'preliminary.detail.corrective.wizard-step-title': 'Corrective Measure Assessment',

        'wizard.finish': 'Finish',
        'wizard.next': 'Next',
        'wizard.previous': 'Previous',
        'wizard.advance-disabled-tooltip': 'Some required values are missing'
    }
};
