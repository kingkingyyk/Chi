USE Chi
@
CREATE TABLE SensorReading(
	SensorName text,
	date text,
	TimeStp timestamp,
	Value double,
	PRIMARY KEY (SensorName, TimeStp)
)