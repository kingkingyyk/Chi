CREATE TABLE Site(
	SiteName varchar(100) PRIMARY KEY,
	SiteMapURL varchar(350)
)
@
CREATE TABLE Controller(
	ControllerName varchar(100) PRIMARY KEY,
	Site varchar(100),
	PositionX double,
	PositionY double,
	IPAddress varchar(15),
	ReportTimeout integer,
	LastReportTime timestamp,
	FOREIGN KEY (Site) REFERENCES Site(SiteName) ON UPDATE CASCADE
)
@
CREATE TABLE SensorClass (
	ClassName varchar(100) PRIMARY KEY
)
@
CREATE TABLE Sensor(
	SensorName varchar(100) PRIMARY KEY,
	Class varchar(100),
	MinValue double,
	MaxValue double,
	TransformationFactor double,
	Unit varchar(10),
	Controller varchar(100),
	MinThreshold double,
	MaxThreshold double,
	PositionX double,
	PositionY double,
	FOREIGN KEY (Class) REFERENCES SensorClass(ClassName) ON UPDATE CASCADE,
	FOREIGN KEY (Controller) REFERENCES Controller(ControllerName) ON UPDATE CASCADE
)
@
CREATE TABLE User(
	Username varchar(100) PRIMARY KEY,
	Password varchar(100),
	Level integer,
	Status varchar(100),
	DateAdded timestamp
)
@
CREATE TABLE Actuator(
	Name varchar(100) PRIMARY KEY,
	Controller varchar(100),
	Status varchar(100),
	PositionX double,
	PositionY double,
	ControlType varchar(100),
	FOREIGN KEY (Controller) REFERENCES Controller(ControllerName) ON UPDATE CASCADE
)
@
CREATE TABLE DayScheduleRule(
	RuleName varchar (100) PRIMARY KEY,
	StartHour int,
	StartMinute int,
	EndHour int,
	EndMinute int
)
@
CREATE TABLE RegularSchedule (
	ScheduleName varchar(100) PRIMARY KEY,
	ActuatorName varchar(100),
	DayMask integer,
	Rule varchar(100),
	OnStartAction varchar(100),
	OnEndAction varchar(100),
	LockManual boolean,
	Priority int,
	Enabled boolean,
	FOREIGN KEY (ActuatorName) REFERENCES Actuator(Name) ON UPDATE CASCADE,
	FOREIGN KEY (Rule) REFERENCES DayScheduleRule(RuleName)  ON UPDATE CASCADE
)
@
CREATE TABLE SpecialSchedule (
	ScheduleName varchar(100) PRIMARY KEY,
	ActuatorName varchar(100),
	Year int,
	Month int,
	Day int,
	Rule varchar(100),
	OnStartAction varchar(100),
	OnEndAction varchar(100),
	LockManual boolean,
	Priority int,
	Enabled boolean,
	FOREIGN KEY (ActuatorName) REFERENCES Actuator(Name) ON UPDATE CASCADE,
	FOREIGN KEY (Rule) REFERENCES DayScheduleRule(RuleName) ON UPDATE CASCADE
)
@
CREATE TABLE SensorActuatorResponse (
	ID int IDENTITY PRIMARY KEY,
	ActuatorName varchar(100),
	OnTriggerAction varchar(100),
	OnNotTriggerAction varchar(100),
	Expression varchar(500),
	Enabled boolean,
	Timeout int,
	FOREIGN KEY (ActuatorName) REFERENCES Actuator(Name) ON UPDATE CASCADE ON DELETE CASCADE
)
@
CREATE TABLE SensorEvent (
	Id bigint IDENTITY PRIMARY KEY,
	SensorName varchar (100),
	TimeStp timestamp,
	EventType varchar (100),
	EventValue varchar(100),
	FOREIGN KEY (SensorName) REFERENCES Sensor(SensorName) ON UPDATE CASCADE
)
@
CREATE TABLE ControllerEvent (
	Id bigint IDENTITY PRIMARY KEY,
	ControllerName varchar (100),
	TimeStp timestamp,
	EventType varchar (100),
	EventValue varchar(100),
	FOREIGN KEY (ControllerName) REFERENCES Controller(ControllerName) ON UPDATE CASCADE
)
@
INSERT INTO Site VALUES ('DefaultSite','http://i.imgur.com/Ep8mS4K.jpg')
@
INSERT INTO SensorClass VALUES ('DefaultClass')
@
INSERT INTO Controller VALUES ('DefaultController','DefaultSite','0.5','0.5','255.255.255.255',10,TIMESTAMP(0))
@
INSERT INTO Site VALUES ('FSKTM Student Center','http://i.imgur.com/p5ZPRNm.jpg')