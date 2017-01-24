# SISEL Release Notes

### 0.10.0 - December 22, 2016
- Support for automatic synchronization with ECCAIRS.

### 0.9.1 - December 19, 2016
- SAG statistics.

### 0.9.0 - December 12, 2016
- Support for SAFA (Feature #188).

### 0.8.1 - December 5, 2016
- Display audit finding level of SAFA audit reports (Feature #245).

### 0.8.0 - November 29, 2016
- Import of reports from ECCAIRS (Feature #189).
- Create new revision from latest ECCAIRS version of a report (Feature #233).
- Distinguish ECCAIRS and SAFA reports in the UI (Feature #235).
- Profile specific JMX bean names (Feature #232).

### 0.7.20 - January 23, 2017
- Made static resources cacheable (Feature #287).
- Spring library upgrade.

### 0.7.19 - December 6, 2016
- Implemented external report filtering - opening application using
URI with report keys as query parameters shows a list of matching reports (Feature #236).

### 0.7.18 - November 21, 2016
- Fixed missing sorting icons in reports table (Bug #226).
- SAFA audit reports are read-only (Feature #224).
- SAFA audit findings contain status and status change date (Feature #227).
- Audit optional contains info about auditor (Feature #221).
- Added safety issue statistics.

### 0.7.17 - November 15, 2016
- Attach references to additional resources to reports (Feature #184).
- Erase user credentials before returning objects from REST services (Feature #219).

### 0.7.16 - November 14, 2016
- - SIRA calculation (Feature #196).
- Move occurrence when its start time changes (Feature #176).

### 0.7.15 - October 31, 2016
- Support for filters in the Reports view (Feature #206).

### 0.7.14 - October 25, 2016
- Delete button in report detail (Feature #201).
- Support for up to two occurrence categories (Feature #157).
- Support for setting SIRA to "Unassigned" (Feature #197).

### 0.7.13 - October 19, 2016
- SIRA support (Feature #187).

### 0.7.12 - October 18, 2016
- Fixed issue in report search - result list items did not lead to their corresponding reports.
- Added support for full text search (Feature #156).

### 0.7.11 - October 17, 2016
- Support for safety issues based on audit findings (Feature #182).
- Mandatory TACR publicity (Feature #185).

### 0.7.10 - October 14, 2016
- Get report by occurrence no longer returns a list of reports, it returns just the report that
documents the occurrence. There cannot be multiple.

### 0.7.9 - October 13, 2016
- Fixed bug in factor graph copying for safety issue base addition (Bug #186).
- Restructured the dashboard (Support #183).

### 0.7.8 - October 4, 2016
- Upgraded to react-bootstrap 0.30.3.
- Upgraded all the related components - datetime picker, typeahead, semforms.

### 0.7.7 - October 3, 2016
- New SISel logo (Support #180).
- New BI views and their switching in the statistics tab (Feature #179).

### 0.7.6 - September 30, 2016
- Allow safety issue base removal (Feature #177).

### 0.7.5 - September 30, 2016
- Fixed issue with unpersisted change exception when audited organization changes in audit report.

### 0.7.4 - September 29, 2016
- Report type filter buttons above the reports table (Feature #173).
- The filter buttons do not show when there is only one kind of reports present.

### 0.7.3 - September 27, 2016
- Fixed issues with report cache (Bug #146).
- Support for more rows per page in tables (Support #152).

### 0.7.2 - September 26, 2016
- Added support for active/inactive state of a safety issue,
- Fixed incorrect logo display in Firefox and missing czech translations.
