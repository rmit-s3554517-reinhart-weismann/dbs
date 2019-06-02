#!/bin/bash
if [ "$1" != '' ]
then

	echo "removing header from csv"
	tail -n +2 "$1" > OSP_DATA.csv.nohead
	echo "compiling code"
	javac *.java
	echo -e "loading heap file\n"
	java dbload -p 4096 OSP_DATA.csv.nohead
	echo "querying heap for street name \"bourke\""
	java dbquery "bourke" 4096
else
	echo "Usage: bash ./part3.sh input_csv_file"
fi
