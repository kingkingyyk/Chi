CREATE TABLE Controller(
	ControllerName varchar(100) PRIMARY KEY
)
@
CREATE TABLE Sensor(
	SensorName varchar(100) PRIMARY KEY,
	Class varchar(100),
	MinValue double,
	MaxValue double,
	TransformationFactor double
)
@
CREATE TABLE ControllerGroup(
	GroupName varchar(100) PRIMARY KEY,
	Controller varchar(100),
	Sensor varchar(100),
	FOREIGN KEY (Controller) REFERENCES Controller(ControllerName),
	FOREIGN KEY (Sensor) REFERENCES Sensor(SensorName)
)
@
CREATE TABLE User(
	Username varchar(100) PRIMARY KEY,
	Password varchar(100),
	Level integer,
	Status varchar(100),
	DateAdded timestamp
)