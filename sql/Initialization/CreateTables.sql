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
	Value double,
	PRIMARY KEY (ControllerName, SensorName, Time)
)
@
CREATE TABLE Sensor(
	SensorName varchar PRIMARY KEY,
	Class varchar,
	MinValue double,
	MaxValue double
)
@
CREATE TABLE User(
	Username varchar PRIMARY KEY,
	Password varchar,
	Level int
)