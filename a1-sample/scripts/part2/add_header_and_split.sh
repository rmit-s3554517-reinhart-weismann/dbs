#!/bin/bash

if [ "$1" != '' ]
then
	tail -n +2 "$1" | split -a 1 -d -l 500000 --additional-suffix=.csv - OSP_DATA_
	for d in OSP_DATA_*
	do
		head -n 1 "$1" | awk -F, '{print $0}' > tmp
		cat $d >> tmp
	   	mv -f tmp $d
	done
else
	echo "Usage: ./add_header_and_split.sh input_csv_file"
fi
