USE Chi
@
CREATE TABLE SensorReading(
	SensorName text,
	Year int,
	Month int,
	Day int,
	Weekday int,
	IsMorning Boolean,
	TimeStp timestamp,
	Value double,
	PRIMARY KEY (SensorName,TimeStp)
) WITH CLUSTERING ORDER BY (TimeStp DESC);
