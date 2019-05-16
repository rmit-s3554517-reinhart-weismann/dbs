#!/bin/bash

if [ "$1" != '' ]
then
	echo "reformatting dates"
	# sed -E 's,([0-9]{2})/([0-9]{2})/([0-9]{4}),\1.\2.\3,g' "$1" > "$1".fixeddates
    python fixdates.py "$1" > "$1".fixeddates
	echo "completed (in $1.fixeddates)"
	echo "splitting data into street_data"
	awk -F, 'BEGIN {OFS=","} NR > 1 {print $8, $9}' "$1".fixeddates | sort -t, -k1n -u > street_data.csv
	echo "completed (in street_data.csv)"
	echo "splitting data into area"
	awk -F, 'BEGIN {OFS=","} NR > 1 {print $7}' "$1".fixeddates | sort -u > area.csv
	echo "completed (in area.csv)"
	echo -e "FALSE,NotPresent\nTRUE,Present" > vehicle_present_options.csv
	echo -e "FALSE,NotInViolation\nTRUE,InViolation" > violation_options.csv
	echo -e "1,centre\n2,north\n3,east\n4,south\n5,west" > side_of_street.csv
	echo "splitting data into devices"
    awk -F, 'BEGIN {OFS=","} NR > 1 {print $1, $10, $11, $12}' "$1".fixeddates | sort -t, -k1n -u > devices.csv
	echo "completed (in devices.csv)"
	echo "splitting data into parking entries"
    awk -F, 'BEGIN {OFS=","} ARGIND < 4 {
        if (1 == ARGIND) {
            street_map[$1] = $1
        } else if (2 == ARGIND) {
            area_map[$1] = FNR
        } else if (3 == ARGIND) {
            device_map[$1] = $1
        }
        next
    }
    FNR > 1 {
        print device_map[$1], $2, $3, $4, $5, $6, area_map[$7], street_map[$8], toupper($13), toupper($14)
    }' street_data.csv area.csv devices.csv "$1".fixeddates > parking.csv
	echo "completed (in parking.csv)"
else
	echo "Usage: ./split-tables.sh input_csv_file"
fi
