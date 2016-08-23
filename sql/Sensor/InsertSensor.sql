LOCK TABLE Sensor WRITE
@
INSERT INTO Sensor (SensorName,Class,MinValue,MaxValue,TransformationFactor,Unit,Controller) VALUES(?,?,?,?,?,?,?)

@
COMMIT