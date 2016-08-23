LOCK TABLE Controller WRITE
@
INSERT INTO Controller (ControllerName,Site,PositionX,PositionY,ReportTimeout,LastReportTime) VALUES(?,?,?,?,?,?)
@
COMMIT