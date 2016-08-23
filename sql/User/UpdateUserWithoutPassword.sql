LOCK TABLE User WRITE
@
UPDATE User SET Level=?, Status=? WHERE Username=?
@
COMMIT