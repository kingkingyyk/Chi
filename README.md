# Chi
A system built for Internet of Things
+ Supports remote management (GWT Web-based UI)
+ Supports automated management (Server GUI + GWT Web-based UI)
+ Web UI repo : https://github.com/zktaichou/fyp

Requires :
+ Apache Cassandra
+ HSQLDB
+ GWT

Server Requirement :
+ Main Server (Tested On : Windows 10)
+ HSQLDB (Tested On : Windows 10, Ubuntu 15)
+ Apache Cassandra (Tested On : Ubuntu 15)
+ Java 8 (Built On Update 92)

Compile :
+ Eclipse MARS.2 / NEON
+ Hibernate Tool (For database schema update)

Database Schema (Paste the XML) :
+ http://ondras.zarovi.cz/sql/demo/ 

Current Working Stuffs :
+ Create database tables
+ Reset database tables
+ User Modelling (+ Store in database)
+ Sensor Modelling (+ Store in database)
+ Site Modelling (+ Store in database)
+ Controller Modelling (+ Store in database)
+ Sensor Class Modelling (+ Store in database)
+ Actuator Modelling (+ Store in database)
+ Day Schedule Rules Modelling (+ Store in database)
+ Regular Schedules Modelling (+ Store in database)
+ Special Schedules Modelling (+ Store in database)
+ Scheduling Time Logic
+ Controller Status Reporting
+ Actuator Status Reporting
+ Actuator control (Manual / Scheduled)
+ Efficient local data caching
+ Notification server
+ Adds Sensor Actuator Response Automation

v1.2 To-Do-List :
+ Release Testing