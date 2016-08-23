LOCK TABLE Controller WRITE
@
UPDATE Controller SET (Site,PositionX,PositionY,ReportTimeout)=(?,?,?,?) WHERE ControllerName=?
@
COMMIT