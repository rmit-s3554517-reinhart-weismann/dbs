#!/bin/bash
for d in OSP_DATA_*
do
	mongoimport -d assignment1 -c parking --type csv --file $d --headerline
done
