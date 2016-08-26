LOCK TABLE User WRITE
@
UPDATE User SET Username=?, Password=?, Level=?, Status=? WHERE Username=?
@
COMMIT