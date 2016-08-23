LOCK TABLE User WRITE
@
UPDATE User SET Password=?, Level=?, Status=? WHERE Username=?
@
COMMIT