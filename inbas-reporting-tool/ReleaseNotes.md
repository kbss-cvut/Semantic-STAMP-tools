# INBAS Reporting Tool Release Notes


### 0.11.3 - September 20, 2017
- Track unsuccessful user login attempts (Task #487).
- Lock account when maximum number of unsuccessful login attempts is reached (Feature #486).
- Admin is able to unlock user account and set new password (Task #493).
- Admin is able to active/deactivate user account (Feature #499).

### 0.11.2 - September 12, 2017
- Fixed the issue with E5X export requiring graphical interface to be present (Bug #479).
- Added ECCAIRS schema into project, so that it does not have to be loaded remotely at runtime (Bug #482).
- Added editable user profile (Feature #494).
- Added default admin account. Credentials are outputted into LOG and into a hidden file in user home on creation (#Task 489).
- Added support for admin-based user registration (admin can add new users) (Task #491).
- Added basic administration screen, currently with a list of all application users (Task #492).
- Report summary wizard is read only - it does not contain the confusing "Finish" button and all inputs are disabled. (Bug #498).
- Upgraded to JOPA 0.9.9.

### 0.11.1 - August 28, 2017
- Support for export to E5X (Feature #88).
- Time difference resolution (Bug #450, Feature #441). 
- Split occurrence start and end specification to date and time (Feature #472).
- Improvements in statistics (Feature #473).

### 0.10.1 - May 4, 2017
- New deployment added (GE).
- Upgraded to JOPA 0.9.6.

### 0.10.0 - April 24, 2017
- Added support for initial reports (Feature #379, Task #383, Task #381).
- Added support for initial report import (Task #382).
- Integrated initial report import with text analysis service (Feature #419, Task #385, Task #386).
- Implementation creation of events for event types extracted by text analysis (Feature #418).
- Implementation of nicer statistics (Feature #420).

### 0.9.2 - March 23, 2017
- Fixed issue with incomplete report removal (Bug #388).
- Modified search result UI to prevent confusion (Bug #387).
- Library update (Sesame 2.8.9 to RDF4J 2.2, JOPA to 0.9.5, semforms to 0.1.2).
- Removed obsolete access to raw repository data.

### 0.9.1 - February 8, 2017
- Rich input support for report summary (Feature #118).

### 0.9.0 - February 6, 2017
- Generated, context-aware, forms support (Feature #328).
- Show loading mask in the touch-enabled factor component when event types are being loaded (Feature #319).

### 0.8.0 - January 28, 2017
- Touch-enabled component replacing gantt-based factor graph editor (Feature #305).
- Improved UI responsiveness, allowing better support for touch and small screen devices (Feature #276).

### 0.7.20 - January 23, 2017
- Made static resources cacheable (Feature #287).
- Spring library upgrade.

### 0.7.19 - December 6, 2016
- Implemented external report filtering - opening application using
URI with report keys as query parameters shows a list of matching reports (Feature #236).

### 0.7.18 - November 21, 2016
- Fixed missing sorting icons in reports table (Bug #226).

### 0.7.17 - November 15, 2016
- Attach references to additional resources to reports (Feature #184).
- Erase user credentials before returning objects from REST services (Feature #219).

### 0.7.16 - November 14, 2016
- Move occurrence when its start time changes (Feature #176).

### 0.7.15 - October 31, 2016
- Support for filters in the Reports view (Feature #206).

### 0.7.10 - October 14, 2016
- Get report by occurrence no longer returns a list of reports, it returns just the report that
documents the occurrence. There cannot be multiple.

### 0.7.8 - October 4, 2016
- Upgraded to react-bootstrap 0.30.3.
- Upgraded all the related components - datetime picker, typeahead, semforms.

### 0.7.4 - September 29, 2016
- Report type filter buttons above the reports table (Feature #173).
- The filter buttons do not show when there is only one kind of reports present.

### 0.7.3 - September 27, 2016
- Fixed issues with report cache (Bug #146).
- Support for more rows per page in tables (Support #152).
