LOCK TABLE User WRITE
@
UPDATE User SET Username=?, Level=?, Status=? WHERE Username=?
@
COMMIT