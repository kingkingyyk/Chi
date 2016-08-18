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
	Username varchar,
	Password varchar,
	Level int,
	Status varchar,
	DateAdded timestamp,
	PRIMARY KEY (Username)
)