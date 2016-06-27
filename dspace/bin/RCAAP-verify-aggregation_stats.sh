#!/bin/bash

#It should be obtained from dspace.cfg -> dbschema and user
user=dspace
db=dspace


#Verify if it's aggregating in the processes
output=`ps aux | grep aggregate | grep -v grep`
#echo $output
set -- $output
pid=$2

#Ok, possibly is running a aggregate process
if [[ "$pid" =~ ^[0-9]+$ ]];
then
 exit	
fi; 


#Verify if there is a control variable in stats.contol
#We only want the value - These are separated by \n, so we only need the thir value
aggdate=$(psql -U $user $db<<<"SELECT agg_end FROM stats.control" | cut -d $'\n' -f3)

#Verify if is date or no date is present
#Stupid verification but it will do
if [[ $aggdate -eq "agg_end" ]];
then
 exit
fi

#echo "agg_end $aggdate"

#Transform the date
aggdate=$(date -d "$aggdate" +"%Y%m%d")

#Add 4 days - We update the table if already four days have passed and no schange in db is seen 
newdate=$(date -d "$aggdate + 4 days" +"%Y%m%d")

#Get actual date
today=$(date +"%Y%m%d")

#echo "newdate $newdate"
#echo "today $today"
 

#Verify if the actual date is 4 days more than the date of agg_end
if [[ "$newdate" > "$today" ]];
then
    exit
fi

#Update Stats control
psql -U $user $db<<<"update stats.control set agg_start= null, agg_end = null;"



