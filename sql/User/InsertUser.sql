LOCK TABLE User WRITE
@
INSERT INTO User(Username,Password,Level,Status,DateAdded) VALUES(?, ?, ?, ?, ?)

@
COMMIT