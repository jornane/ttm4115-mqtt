TTM4115 term assignment - Basic MQTT blocks
===========================================

In the term assignment for TTM4115, students are required to make a taxi simulator where taxis drive around on a map shown on a tablet device.  [The map application on the tablet is provided](https://play.google.com/store/apps/details?id=com.bitreactive.apps.genericmapui), but the points on the map must be provided by code written by the students.  The protocol for communicating with the Generic Map UI is MQTT.

This project contains two blocks, one generic block **BasicMQTT** for sending plain Strings over MQTT and one specific block **MapMQTT** which uses *BasicMQTT* to support sending MapUpdate objects to the Generic Map UI.  Students are expected to use **MapMQTT** for communication with the Generic Map UI.  Students should make their own **TaxiMQTT** block using *BasicMQTT* which allows taxis to communicate with a dispatch service that tells them where to pick up customers.

The blocks are created using [Reactive Blocks](http://www.bitreactive.com/technology/), which requires a license to compile the software.  The blocks themselves are available both on [Github](https://github.com/yorn/ttm4115-mqtt) and Reactive Blocks' own library.