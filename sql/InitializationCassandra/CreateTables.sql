USE Chi
@
CREATE TABLE SensorReading(
	ControllerName varchar,
	SensorName varchar,
	TimeStp timestamp,
	DayInWeek int,
	Day int,
	Month int,
	Year int,
	Hour int,
	Minute int,
	Second int,
	Value double,
	PRIMARY KEY ((ControllerName, SensorName, Year, Month, Day), Hour, Minute, Second)
)