Part 1 DERBY:
-------------

structure:
----------
OSP_STREET(STREET_ID*, NAME)
OSP_AREA(AREA_ID* ,NAME)
OSP_VIOLATION_OPTIONS(VIOLATION_OPT_ID*, IN_VIOLATION)
OSP_SIDE_STREET(STREET_ID*, NAME)
OSP_DEVICE(DEVICE_ID*, BT_STREET_1, BT_STREET_2, LOCATION)
OSP_PARKING(ID*, _DEVICE_ID_, ARRIVE, DEPART, DURATION, STREET_MARKER,
            PARKING_SIGN, _AREA_ID_, _STREET_ID_, _VEHICLE_OPT_ID_,
            _VIOLATION_OPT_ID_)



steps:
------
bash part1.sh On-street_Car_Parking_Sensor_Data_-_2017.csv

====================================================================

Part 2 MONGODB:
---------------

structure:
----------
{
	"_id" : ObjectId("5cbd733ac693aa79d84e849d"),
	"DeviceId" : 18773,
    "Duration": {
        "ArrivalTime" : "04/11/2017 07:24:35 AM",
        "DepartureTime" : "04/11/2017 07:30:00 AM",
        "DurationSeconds" : 325,
    },
	"StreetMarker" : "2477S",
	"Sign" : "",
	"Area" : "Spencer",
	"StreetId" : 123,
	"StreetName" : "BOURKE STREET",
	"BetweenStreet1" : "SPENCER STREET",
	"BetweenStreet2" : "KING STREET",
	"Side Of Street" : 4,
	"In Violation" : "False",
}

steps:
------
bash part2.sh On-street_Car_Parking_Sensor_Data_-_2017.csv

====================================================================

Part 3 HEAP:
------------

structure:
----------
FIXED size fields:

   total RECORD_SIZE = 336

   EOF_PAGENUM_SIZE = 4
   RID_SIZE = 4
   DEVICE_ID_SIZE = 6
   ARRIVAL_TIME_SIZE = 22
   DEPART_TIME_SIZE = 22
   DURATION_SIZE = 10
   STREET_MARKER_SIZE = 12
   SIGN_SIZE = 50
   AREA_SIZE = 20
   STREET_ID_SIZE = 10
   STREET_NAME_SIZE = 50
   BETWEEN_STREET_1_SIZE = 50
   BETWEEN_STREET_2_SIZE = 50
   SIDE_OF_STREET_SIZE = 1
   VIOLATION_SIZE = 5



steps:
------

bash part3.sh On-street_Car_Parking_Sensor_Data_-_2017.csv

This will:   

remove header (first line in csv file)

tail -n +2 On-street_Car_Parking_Sensor_Data_-_2017.csv > On-street_Car_Parking_Sensor_Data_-_2017.csv.nohead


then compile:

javac *.java

then create heap file (eg 4096 byte pages):

java dbload -p 4096 On-street_Car_Parking_Sensor_Data_-_2017.csv.nohead

then query:

java dbquery "bourke" 4096
