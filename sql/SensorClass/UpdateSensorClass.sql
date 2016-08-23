LOCK TABLE SensorClass WRITE
@
UPDATE SensorClass SET ClassName=? WHERE ClassName=?
@
COMMIT