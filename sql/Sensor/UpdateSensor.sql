UPDATE Sensor SET (SensorName,Class,MinValue,MaxValue,TransformationFactor,Unit) = (?,?,?,?,?)
 WHERE SensorName=?
@
COMMIT