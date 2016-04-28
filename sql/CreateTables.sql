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
	PRIMARY KEY (ControllerName, SensorName)
)
@
CREATE TABLE Sensor(
	SensorName varchar PRIMARY KEY,
	Class varchar,
	Expression varchar,
	MinValue double,
	MaxValue double
)