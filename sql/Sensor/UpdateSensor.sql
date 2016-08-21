UPDATE Sensor SET (Class,MinValue,MaxValue,TransformationFactor,Unit,Controller) = (?,?,?,?,?,?)
 WHERE SensorName=?
@
COMMIT