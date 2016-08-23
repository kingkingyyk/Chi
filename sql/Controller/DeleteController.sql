LOCK TABLE Controller WRITE
@
DELETE FROM Controller WHERE ControllerName=?

@
COMMIT