USE Chi
@
CREATE TABLE SensorReading(
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
	PRIMARY KEY ((SensorName, Year, Month, Day), Hour, Minute, Second)
)