# Chi
A Internet of Things automation system:
+ Supports distributed, high availability and scalability data storage through Apache Cassandra
+ Supports variety of clients through REST API
+ Supports creation of virtual sensor from multiple sensors
+ Automated actuator control through schedule or sensor reading
+ Award winning
![alt text](http://i.imgur.com/aBeVFAG.jpg)

Requires :
+ Apache Cassandra

## Planned Features & Improvements in this release :
### Code
+ Rebuild everything
+ Refactor objects

### Communication
+ REST API for everything
+ Update subscription

### Client
+ JavaFX REST Client
+ Android Client

### Data
+ Remove the usage of HSQLDB
+ Support for multiples Cassandra servers
+ Async data processing on incoming queries
+ Improve prediction

### IoT Node
+ Virtual(N-th order) sensor
+ Improve sensor reading automation logic
+ Support for expression-based sensor reading calibration