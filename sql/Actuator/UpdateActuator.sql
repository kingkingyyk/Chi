LOCK TABLE Actuator WRITE
@
UPDATE Actuator SET (Name,Controller)=(?,?) WHERE Name=?

@
COMMIT