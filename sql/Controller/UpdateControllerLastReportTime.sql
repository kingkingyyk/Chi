LOCK TABLE Controller WRITE
@
UPDATE Controller SET LastReportTime=? WHERE ControllerName=?

@
COMMIT