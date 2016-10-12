USE Chi
@
CREATE TABLE SensorReading(
	Id uuid,
	SensorName text,
	Year int,
	Month int,
	Day int,
	Weekday int,
	IsMorning Boolean,
	TimeStp timestamp,
	Value double,
	PRIMARY KEY (Id,TimeStp)
) WITH CLUSTERING ORDER BY (TimeStp DESC);
