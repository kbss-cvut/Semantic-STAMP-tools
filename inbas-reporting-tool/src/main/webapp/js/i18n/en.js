/**
 * English localization.
 */

const Constants = require('../constants/Constants');

module.exports = {
    'locale': 'en',

    'messages': {
        'add': 'Add',
        'back': 'Go Back',
        'cancel': 'Cancel',
        'open': 'Open',
        'close': 'Close',
        'cancel-tooltip': 'Discard changes',
        'save': 'Save',
        'delete': 'Delete',
        'exportToE5X': 'export to E5X',
        'remove': 'Remove',
        'headline': 'Headline',
        'summary': 'Summary',
        'narrative': 'Narrative',
        'fileNo': 'File number',
        'table-actions': 'Actions',
        'table-edit': 'Edit',
        'save-success-message': 'Report successfully saved.',
        'save-failed-message': 'Unable to save report. Server responded with message: ',
        'author': 'Author',
        'author-title': 'Report author',
        'description': 'Description',
        'select.default': '--- Select ---',
        'yes': 'Yes',
        'no': 'No',
        'unknown': 'Unknown',
        'uploading-mask': 'Uploading',
        'please-wait': 'Please wait...',
        'issue-fix': 'Fix the issue',

        'detail.save-tooltip': 'Save changes',
        'detail.saving': 'Saving...',
        'detail.invalid-tooltip': 'Some of the required values are missing',
        'detail.large-time-diff-tooltip': 'Occurrence start and end time difference is too large.',
        'detail.large-time-diff-event-tooltip': 'Time difference between occurrence and its sub-events is too large.',
        'detail.submit': 'Create new revision',
        'detail.submit-tooltip': 'Create new revision of this report',
        'detail.submit-success-message': 'Report successfully submitted.',
        'detail.submit-failed-message': 'Unable to submit report. Server responded with message: ',
        'detail.phase-transition-success-message': 'Report phase transition successful.',
        'detail.phase-transition-failed-message': 'Report phase transition failed with error: ',
        'detail.loading': 'Loading report...',
        'detail.not-found.title': 'Report not found',
        'detail.fix.title': 'Fix report',
        'detail.fix.start-time': 'Start time',
        'detail.fix.end-time': 'End time',
        'detail.fix.done': 'Done',
        'detail.fix.done.tooltip': 'Report is fixed and ready to be displayed now',
        'detail.fix.time-diff-hint': 'Maximum difference between occurrence/event start and end time can be {value, plural, one {# hour} other {# hours}}.',

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
        'register.error': 'Unable to register user account.',
        'register.login.error': 'Unable to login into the newly created account.',

        'main.dashboard-nav': 'Dashboards',
        'main.reports-nav': 'Reports',
        'main.statistics-nav': 'Statistics',
        'main.admin-nav': 'Administration',
        'main.user-profile': 'User profile',
        'main.logout': 'Logout',
        'main.search-placeholder': 'Search',
        'main.search.fulltext': 'Search in all narratives',
        'main.search.fulltext.label': 'Full text',
        'main.search.fulltext-tooltip': 'Search for the specified string everywhere',

        'dashboard.welcome': 'Hello {name}, Welcome to ' + Constants.APP_NAME + '.',
        'dashboard.create-tile': 'Create Report',
        'dashboard.search-tile': 'Search for Report',
        'dashboard.search-placeholder': 'Report headline',
        'dashboard.view-all-tile': 'View All Reports',
        'dashboard.create-empty-tile': 'Start with Empty Report',
        'dashboard.create-import-tile': 'Import Report',
        'dashboard.recent-panel-heading': 'Recently Edited/Added Reports',
        'dashboard.recent-table-last-edited': 'Last modified',
        'dashboard.recent.no-reports': 'There are no reports, yet.',
        'dashboard.import.import-e5': 'Import E5X/E5F report',
        'dashboard.import-initial-tile': 'Import Initial Report',

        'dashboard.unprocessed': 'You have {count} unprocessed report(s).',

        'dropzone.title': 'Drop the file here or click to select a file to upload.',
        'dropzone-tooltip': 'Click here to select a file to upload',

        'reports.no-reports': 'There are no reports, yet. You can create one ',
        'reports.no-reports.link': 'here.',
        'reports.no-reports.link-tooltip': 'Go to dashboard',
        'reports.open-tooltip': 'Click to see report detail and edit it',
        'reports.delete-tooltip': 'Delete this report',
        'reports.loading-mask': 'Loading reports...',
        'reports.panel-title': 'Reports',
        'reports.table-date': 'Date',
        'reports.table-date.tooltip': 'Date and time of the reported event',
        'reports.table-moreinfo': 'Additional info',
        'reports.table-type': 'Report type',
        'reports.table-classification': 'Category',
        'reports.table-classification.tooltip': 'Select occurrence category to show',
        'reports.phase': 'Report state',
        'reports.filter.label': 'Filter',
        'reports.filter.type.tooltip': 'Select report type',
        'reports.filter.type.all': 'All',
        'reports.filter.type.label': 'Report type filters:',
        'reports.filter.no-matching-found': 'No reports match the selected filters.',
        'reports.filter.reset': 'Reset filters',
        'reports.paging.item-count': 'Showing {showing} of {total} items.',
        'reports.create-report': 'Create report',
        'reports.unable-to-load': 'Unable to load reports. Check the browser console for more details.',

        'filters.label': 'Filters',

        'delete-dialog.title': 'Delete {type} Report?',
        'delete-dialog.content': 'Are you sure you want to remove this report?',

        'occurrence.headline-tooltip': 'Short descriptive summary of the occurrence - this field is required',
        'occurrence.start-time': 'Occurrence start',
        'occurrence.start-time-tooltip': 'Date and time when the event occurred.\nNote: changing start time moves the whole occurrence in time, changing its end time modifies its duration.',
        'occurrence.start.date-tooltip': 'Date when the event occurred.\nNote: changing start time moves the whole occurrence in time, changing its end time modifies its duration.',
        'occurrence.start.time-tooltip': 'Time when the event occurred.\nNote: changing start time moves the whole occurrence in time, changing its end time modifies its duration.',
        'occurrence.end-time': 'Occurrence end',
        'occurrence.end-time-tooltip': 'Date and time when the event ended',
        'occurrence.end.date-tooltip': 'Date when the event ended',
        'occurrence.end.time-tooltip': 'Time when the event ended',
        'occurrence.class': 'Occurrence class',
        'occurrence.class-tooltip': 'Occurrence class - this field is required',

        'report.initial.import.title': 'Initial report import',
        'report.initial.import.run': 'Import',
        'report.initial.import.text.tooltip': 'Enter the initial report text here',
        'report.initial.import.importing-msg': 'Analyzing the initial report',
        'report.initial.text.label': 'Text',
        'report.initial.label': 'Initial report',
        'report.initial.view.tooltip': 'View initial report',
        'report.initial.analysis-results.label': 'Report analysis results',

        'report.summary': 'Report summary',
        'report.created-by-msg': 'Created {date} by {name}.',
        'report.last-edited-msg': 'Last modified {date} by {name}.',
        'report.narrative-tooltip': 'Narrative - this field is required',
        'report.table-edit-tooltip': 'Edit statement',
        'report.table-delete-tooltip': 'Delete statement',
        'report.corrective.panel-title': 'Corrective measures',
        'report.corrective.table-description': 'Corrective measure',
        'report.corrective.description-placeholder': 'Corrective measure description',
        'report.corrective.description-tooltip': 'Corrective measure description - field is required',
        'report.corrective.add-tooltip': 'Add a corrective measure',
        'report.corrective.wizard.title': 'Corrective Measure Wizard',
        'report.corrective.wizard.step-title': 'Corrective Measure Assessment',
        'report.eventtype.table-type': 'Event type',
        'report.eventtype.add-tooltip': 'Add an event type assessment',
        'report.organization': 'Organization',
        'report.responsible-department': 'Responsible department',
        'report.attachments.title': 'Attachments',
        'report.attachments.create.button': 'Attach',
        'report.attachments.create.reference-label': 'Value',
        'report.attachments.create.reference-tooltip': 'Reference value, for example a document URL - this field is required',
        'report.attachments.create.description-label': 'Description',
        'report.attachments.create.description-tooltip': 'Optional description of the attachment',
        'report.attachments.table.reference': 'Reference',
        'report.attachments.save.disabled-tooltip': 'Attachment value is required',

        'report.occurrence.category.label': 'Occurrence category',
        'occurrencereport.title': 'Occurrence report',
        'occurrencereport.label': 'Occurrence',

        'wizard.finish': 'Finish',
        'wizard.next': 'Next',
        'wizard.previous': 'Previous',
        'wizard.advance-disabled-tooltip': 'Some required values are missing',

        'eventtype.title': 'Event type',
        'eventtype.default.description': 'Description',
        'eventtype.default.description-placeholder': 'Event description',


        'factors.panel-title': 'Factors',
        'factors.scale': 'Scale',
        'factors.scale-tooltip': 'Click to select scale: {unit}',
        'factors.scale.second': 'Seconds',
        'factors.scale.minute': 'Minutes',
        'factors.scale.hour': 'Hours',
        'factors.scale.relative': 'Relative',
        'factors.scale.relative-tooltip': 'Click to select relative scale',
        'factors.link-type-select': 'Factor relationship type?',
        'factors.link-type-select-tooltip': 'Select link type',
        'factors.link.delete.title': 'Delete link?',
        'factors.link.delete.text': 'Are you sure you want to delete the link from {source} to {target}?',
        'factors.event.label': 'Event',
        'factors.detail.title': 'Factor assessment',
        'factors.detail.type': 'Type',
        'factors.detail.type-placeholder': 'Factor type',
        'factors.detail.time-period': 'Time period',
        'factors.detail.start': 'Start time',
        'factors.detail.duration': 'Duration',
        'factors.duration.second': '{duration, plural, one {second} other {seconds}}',
        'factors.duration.minute': '{duration, plural, one {minute} other {minutes}}',
        'factors.duration.hour': '{duration, plural, one {hour} other {hours}}',
        'factors.detail.details': 'Details',
        'factors.detail.delete.title': 'Delete factor?',
        'factors.detail.delete.text': 'Are you sure you want to remove this factor?',
        'factors.detail.wizard-loading': 'Generating form...',
        'factors.smallscreen.start': 'Start',
        'factors.smallscreen.end': 'End',
        'factors.smallscreen.add-tooltip': 'Add event',
        'factors.event-suggested': 'Event suggested by initial report text analysis.',

        'notfound.title': 'Not found',
        'notfound.msg-with-id': '{resource} with id {identifier} not found.',
        'notfound.msg': '{resource} not found.',

        'notrenderable.title': 'Unable to display report',
        'notrenderable.error': 'Error: {message}',
        'notrenderable.error-generic': 'Please verify report validity.',

        'revisions.label': 'Revisions',
        'revisions.created': 'Created',
        'revisions.show-tooltip': 'Show this revision',
        'revisions.readonly-notice': 'Older revisions are read-only.',

        'sort.no': 'Click to sort records by this column',
        'sort.asc': 'Records are sorted in ascending order',
        'sort.desc': 'Records are sorted in descending order',

        'search.loading': 'Searching...',
        'search.title': 'Search results',
        'search.headline': 'Search for {expression} found {count, plural, one {# result} other {# results}}.',
        'search.results.match': 'Matching snippet',

        'validation.error.start-after-end': 'Error: end time cannot occur before start time',

        'editor.rich.h1': 'Heading 1',
        'editor.rich.h2': 'Heading 2',
        'editor.rich.h3': 'Heading 3',
        'editor.rich.h4': 'Heading 4',
        'editor.rich.h5': 'Heading 5',
        'editor.rich.h6': 'Heading 6',
        'editor.rich.body': 'Body',
        'editor.rich.ul': 'Bullets',
        'editor.rich.ol': 'Numbering',
        'editor.rich.blockquote': 'Blockquote',
        'editor.rich.bold': 'Bold',
        'editor.rich.italic': 'Italic',
        'editor.rich.underline': 'Underline',

        'profile.header': 'User profile',
        'profile.invalid': 'User profile data are not valid. There are attributes missing.',
        'profile.originalPassword': 'Original password',
        'profile.update.success': 'User profile successfully updated.',
        'profile.update.error': 'Profile update failed. Server responded with message: ',
        'profile.password.toggle': 'Change password',
        'profile.password.original': 'Original password',
        'profile.password.new': 'New password',
        'profile.password.confirm': 'Confirm password',
        'profile.username.exists': 'Username already exists',

        'users.title': 'Users',
        'users.table.name': 'Name',
        'users.table.username': 'Username',
        'users.table.status': 'Account status',
        'users.table.blocked.tooltip': 'Account is blocked!',
        'users.table.not.blocked.tooltip': 'Account is accessible'
    }
};
