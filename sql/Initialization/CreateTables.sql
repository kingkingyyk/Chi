USE Chi
@
CREATE TABLE Controller(
	ControllerName varchar PRIMARY KEY
)
@
CREATE TABLE ControllerGroup(
	GroupName varchar PRIMARY KEY,
	ReadingController varchar,
	ActingController varchar
)
@
CREATE TABLE SensorReading(
	ControllerName varchar,
	SensorName varchar,
	Time timestamp,
	DayInWeek int,
	Day int,
	Month int,
	Year int,
	Hour int,
	Minute int,
	Second int,
	Value double,
	PRIMARY KEY (ControllerName, SensorName, Day, Month, Year, Hour, Minute, Second)
)
@
CREATE TABLE Sensor(
	SensorName varchar PRIMARY KEY,
	Class varchar,
	MinValue double,
	MaxValue double,
	TransformationFactor double
)
@
CREATE TABLE User(
	Username varchar PRIMARY KEY,
	Password varchar,
	Level int
)