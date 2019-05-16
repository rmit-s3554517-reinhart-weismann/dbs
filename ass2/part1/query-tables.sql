connect 'jdbc:derby:OSP_DB';

SHOW tables in APP;

select p.ID, p.DEVICE_ID, ARRIVE, DEPART, DURATION, STREET_MARKER, PARKING_SIGN, s.NAME, BT_STREET_1, BT_STREET_2, LOCATION, o.PRESENT
from OSP_PARKING p, OSP_DEVICE d, OSP_STREET s, OSP_VEHICLE_OPTIONS o
where p.device_id = d.device_id
and p.street_id = s.street_id
and p.vehicle_opt_id = o.vehicle_opt_id
and p.id > 1
and p.id < 10;
