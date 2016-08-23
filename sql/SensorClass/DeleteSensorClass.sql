LOCK TABLE Sensor WRITE, SensorClass WRITE
@
UPDATE Sensor SET Class='DefaultClass' WHERE Class=?
@
DELETE FROM SensorClass WHERE ClassName=?
@
COMMIT