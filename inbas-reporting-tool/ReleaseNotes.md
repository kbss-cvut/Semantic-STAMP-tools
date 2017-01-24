# INBAS Reporting Tool Release Notes

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
