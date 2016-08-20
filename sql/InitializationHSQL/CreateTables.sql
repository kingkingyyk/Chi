CREATE TABLE Site(
	SiteName varchar(100) PRIMARY KEY,
	SiteMapURL varchar(350)
)
@
CREATE TABLE Controller(
	ControllerName varchar(100) PRIMARY KEY,
	Site varchar(100),
	PositionX double,:
	PositionY double,
	ReportTimeout integer,
	LastReportTime timestamp,
	FOREIGN KEY (Site) REFERENCES Site(SiteName)
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
	FOREIGN KEY (Class) REFERENCES SensorClass(ClassName)
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
@
INSERT INTO Site VALUES ('DefaultSite','http://i.imgur.com/Ep8mS4K.jpg')
@
INSERT INTO SensorClass VALUES ('DefaultClass')