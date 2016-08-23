LOCK TABLE Actuator WRITE
@
UPDATE Actuator SET Status=? WHERE Name=?

@
COMMIT