UPDATE Sensor SET (Class,MinValue,MaxValue,TransformationFactor,Unit) = (?,?,?,?)
 WHERE SensorName=?
@
COMMIT