connect 'jdbc:derby:OSP_DB;create=true';

/* drop order matters */
DROP TABLE OSP_PARKING;
DROP TABLE OSP_DEVICE;

DROP TABLE OSP_SIDE_STREET;
DROP TABLE OSP_STREET;
DROP TABLE OSP_AREA;
DROP TABLE OSP_VIOLATION_OPTIONS;


CREATE TABLE OSP_STREET(STREET_ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH
        1, INCREMENT BY 1) PRIMARY KEY, NAME VARCHAR(200));

CREATE TABLE OSP_AREA(AREA_ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH
        1, INCREMENT BY 1) PRIMARY KEY, NAME VARCHAR(200));

CREATE TABLE OSP_VIOLATION_OPTIONS(VIOLATION_OPT_ID BOOLEAN NOT NULL PRIMARY KEY, IN_VIOLATION VARCHAR(20));

CREATE TABLE OSP_SIDE_STREET(STREET_ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH
        1, INCREMENT BY 1) PRIMARY KEY, NAME VARCHAR(10));

CREATE TABLE OSP_DEVICE(DEVICE_ID INT NOT NULL GENERATED BY DEFAULT AS IDENTITY
    (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    BT_STREET_1 VARCHAR(100),
    BT_STREET_2 VARCHAR(100),
    LOCATION INT REFERENCES OSP_SIDE_STREET(STREET_ID));

CREATE TABLE OSP_PARKING(ID INT NOT NULL GENERATED ALWAYS AS IDENTITY
    (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    DEVICE_ID INT REFERENCES OSP_DEVICE(DEVICE_ID),
    ARRIVE DATE,
    DEPART DATE,
    DURATION BIGINT,
    STREET_MARKER VARCHAR(50),
    PARKING_SIGN VARCHAR(50),
    AREA_ID INT REFERENCES OSP_AREA(AREA_ID),
    STREET_ID INT REFERENCES OSP_STREET(STREET_ID),
    VIOLATION_OPT_ID BOOLEAN REFERENCES OSP_VIOLATION_OPTIONS(VIOLATION_OPT_ID));


CALL SYSCS_UTIL.SYSCS_IMPORT_DATA (NULL, 'OSP_STREET', 'STREET_ID, NAME', NULL, 'street_data.csv', NULL, NULL, NULL, 0);
CALL SYSCS_UTIL.SYSCS_IMPORT_DATA (NULL, 'OSP_AREA', 'NAME', NULL, 'area.csv', NULL, NULL, NULL, 0);
CALL SYSCS_UTIL.SYSCS_IMPORT_DATA (NULL, 'OSP_VIOLATION_OPTIONS', NULL, NULL, 'violation_options.csv', NULL, NULL, NULL, 0);
CALL SYSCS_UTIL.SYSCS_IMPORT_DATA (NULL, 'OSP_SIDE_STREET', NULL, NULL, 'side_of_street.csv', NULL, NULL, NULL, 0);
CALL SYSCS_UTIL.SYSCS_IMPORT_DATA (NULL, 'OSP_DEVICE', NULL, NULL, 'devices.csv', NULL, NULL, NULL, 0);
CALL SYSCS_UTIL.SYSCS_IMPORT_DATA (NULL, 'OSP_PARKING', 'DEVICE_ID,ARRIVE,DEPART,DURATION,STREET_MARKER,PARKING_SIGN,AREA_ID,STREET_ID,VIOLATION_OPT_ID', NULL, 'parking.csv', NULL, NULL, NULL, 0);
